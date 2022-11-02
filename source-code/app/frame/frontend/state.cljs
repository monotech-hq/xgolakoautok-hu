
(ns app.frame.frontend.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (keyword)
(defonce VISIBLE-MENU (ratom nil))
