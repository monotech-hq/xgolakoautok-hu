
(ns app.price-quotes.frontend.handler.state
    (:require [reagent.api :refer [ratom]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (object)
(defonce PDF-DOWNLOAD-STATUS (ratom nil))

; @atom (object)
(defonce PDF-OBJECT-URL (ratom nil))
