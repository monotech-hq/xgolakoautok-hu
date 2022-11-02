
(ns app.contents.frontend.handler.helpers
    (:require [app-fruits.html   :as html]
              [mid-fruits.string :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn parse-content-body
  ; @param (string) body
  ;
  ; @example
  ;  (parse-content-body "<p>Paragraph #1</p><p>Paragraph #2</p>")
  ;  =>
  ;  [:div [:p "Paragraph #1"] [:p "Paragraph #2"]]
  ;
  ; @return (vector)
  [body]
  ; Az app-fruits.html/to-hiccup függvény az átadott html string első html elemét
  ; konvertálja hiccup típusra, ezért szükséges a tartalmat egy div elembe helyezni,
  ; hogy az egész tartalom az első elemben legyen!
  (if (string/nonempty? body)
      (html/to-hiccup (str "<div>"body"</div>"))))
