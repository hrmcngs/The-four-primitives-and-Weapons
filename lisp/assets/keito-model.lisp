;;; Keep Ketu's geometry in sync with the shared katana, adding a second blade tone.
(load "lisp/common/core.lisp")
(in-package :maw-tools)
(let* ((root "src/main/resources/assets/the_four_primitives_and_weapons/models/")
       (base (parse-json (read-text (concatenate 'string root "custom/weapon/katana/katana_a_parent.json"))))
       (elements (gethash "elements" base))
       (changed 0))
  (loop for element across elements do
    (maphash (lambda (direction face)
               (when (and (eql (gethash "tintindex" face) 6)
                          (member direction '("south" "west" "down") :test #'equal))
                 (setf (gethash "tintindex" face) 7)
                 (incf changed)))
             (gethash "faces" element)))
  (assert (> changed 0))
  (write-text (concatenate 'string root "custom/keito_katana.json")
              (encode (object "parent" "the_four_primitives_and_weapons:custom/katanairon3d"
                              "elements" elements) t))
  (format t "Keito blade: ~D secondary-tone faces generated.~%" changed))
