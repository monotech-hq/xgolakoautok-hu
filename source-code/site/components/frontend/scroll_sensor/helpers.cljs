
(ns site.components.frontend.scroll-sensor.helpers
    (:require [dom.api :as dom]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sensor-did-mount-f
  ; @param (keyword) sensor-id
  ; @param (function) callback-f
  [sensor-id callback-f]
  (let [sensor-element (-> sensor-id name dom/get-element-by-id)]
       (dom/setup-intersection-observer! sensor-element callback-f)))
