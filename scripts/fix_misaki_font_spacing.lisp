;;; Run: sbcl --script scripts/fix_misaki_font_spacing.lisp
(load (merge-pathnames "misaki-font.lisp" *load-truename*))
(let ((corrected (misaki-font:fix-font
                  (misaki-font:read-bytes (merge-pathnames "misaki_gothic.ttf" misaki-font:*pack-font*)))))
  (dolist (folder (list misaki-font:*pack-font* misaki-font:*base-font*))
    (misaki-font:write-bytes (merge-pathnames "misaki_gothic_minecraft.ttf" folder) corrected))
  (format t "Built Minecraft font with unchanged glyph outlines and advance widths.~%"))
