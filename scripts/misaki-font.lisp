;;; Minecraft-specific Misaki spacing correction, using only Common Lisp.
;;; Outlines and advance widths stay intact. Minecraft already includes the
;;; outline's x offset, so its additional hmtx left bearing must be zero.
;;; Align the vertical baseline to Minecraft's bitmap font as well. Moving
;;; ascent and descent together preserves STB's scale and every glyph's size.
(defpackage :misaki-font
  (:use :cl)
  (:export :*root* :*pack-font* :*base-font* :read-bytes :write-bytes
           :uint :tables :table-record :table-offset :table-length
           :font-checksum :fix-font))
(in-package :misaki-font)

(defparameter *root*
  (truename (merge-pathnames "../" (make-pathname :name nil :type nil :defaults *load-truename*))))
(defparameter *pack-font*
  (merge-pathnames "src/main/resources/resourcepacks/misaki_font_pack/assets/the_four_primitives_and_weapons/font/" *root*))
(defparameter *base-font*
  (merge-pathnames "src/main/resources/assets/the_four_primitives_and_weapons/font/" *root*))

(defun read-bytes (path)
  (with-open-file (stream path :element-type '(unsigned-byte 8))
    (let ((bytes (make-array (file-length stream) :element-type '(unsigned-byte 8))))
      (assert (= (read-sequence bytes stream) (length bytes)))
      bytes)))

(defun write-bytes (path bytes)
  (with-open-file (stream path :direction :output :element-type '(unsigned-byte 8)
                          :if-exists :supersede :if-does-not-exist :create)
    (write-sequence bytes stream)))

(defun uint (bytes offset size)
  (assert (<= 0 offset (+ offset size) (length bytes)))
  (loop for i from offset below (+ offset size)
        for value = (aref bytes i) then (+ (ash value 8) (aref bytes i))
        finally (return value)))

(defun put-uint (bytes offset size value)
  (assert (<= 0 offset (+ offset size) (length bytes)))
  (dotimes (i size)
    (setf (aref bytes (+ offset i)) (ldb (byte 8 (* 8 (- size i 1))) value))))

(defun signed16 (value)
  (if (>= value #x8000) (- value #x10000) value))

(defstruct table record offset length)

(defun tables (bytes)
  (let ((result (make-hash-table :test 'equal)))
    (dotimes (i (uint bytes 4 2))
      (let* ((record (+ 12 (* i 16)))
             (tag (map 'string #'code-char (subseq bytes record (+ record 4))))
             (offset (uint bytes (+ record 8) 4))
             (length (uint bytes (+ record 12) 4)))
        (assert (<= (+ offset length) (length bytes)))
        (setf (gethash tag result) (make-table :record record :offset offset :length length))))
    result))

(defun font-checksum (bytes &optional (start 0) (end (length bytes)))
  (logand #xffffffff
          (loop for offset from start below end by 4
                sum (loop for i from 0 below 4
                          sum (if (< (+ offset i) end)
                                  (ash (aref bytes (+ offset i)) (* 8 (- 3 i))) 0)))))

(defun fix-font (original)
  (let* ((bytes (copy-seq original))
         (tables (tables bytes))
         (hhea (gethash "hhea" tables))
         (maxp (gethash "maxp" tables))
         (hmtx (gethash "hmtx" tables))
         (head (gethash "head" tables))
         (metrics (uint bytes (+ (table-offset hhea) 34) 2))
         (glyphs (uint bytes (+ (table-offset maxp) 4) 2)))
    (assert (<= metrics glyphs))
    (dotimes (i metrics)
      (put-uint bytes (+ (table-offset hmtx) (* i 4) 2) 2 0))
    (dotimes (i (- glyphs metrics))
      (put-uint bytes (+ (table-offset hmtx) (* metrics 4) (* i 2)) 2 0))
    (let* ((hhea-start (table-offset hhea))
           (ascent (signed16 (uint bytes (+ hhea-start 4) 2)))
           (descent (signed16 (uint bytes (+ hhea-start 6) 2)))
           (span (- ascent descent))
           (units (uint bytes (+ (table-offset head) 18) 2))
           ;; At size 8 Minecraft subtracts 3 from the TTF glyph's top.
           ;; Misaki's full-height glyph ymax is 6/8 em; ascent 9/8 em
           ;; cancels that subtraction, aligning it with the ASCII bitmap.
           (target-ascent (round (* units 9/8))))
      (put-uint bytes (+ hhea-start 4) 2 target-ascent)
      (put-uint bytes (+ hhea-start 6) 2 (- target-ascent span))
      (put-uint bytes (+ (table-record hhea) 4) 4
                (font-checksum bytes hhea-start (+ hhea-start (table-length hhea)))))
    (put-uint bytes (+ (table-record hmtx) 4) 4
              (font-checksum bytes (table-offset hmtx) (+ (table-offset hmtx) (table-length hmtx))))
    (put-uint bytes (+ (table-offset head) 8) 4 0)
    (put-uint bytes (+ (table-record head) 4) 4
              (font-checksum bytes (table-offset head) (+ (table-offset head) (table-length head))))
    (put-uint bytes (+ (table-offset head) 8) 4
              (logand #xffffffff (- #xb1b0afba (font-checksum bytes))))
    (assert (= (font-checksum bytes) #xb1b0afba))
    bytes))
