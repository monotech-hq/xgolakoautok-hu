
(ns site.components.frontend.scroll-sensor.views
    (:require [mid-fruits.random                              :as random]
              [reagent.api                                    :as reagent]
              [site.components.frontend.scroll-sensor.helpers :as scroll-sensor.helpers]))
   
;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) sensor-id
  ; @param (function) callback-f
  ;
  ; @usage
  ;  [scroll-sensor ...]
  ;
  ; @usage
  ;  [scroll-sensor :my-sensor ...]
  ;
  ; @usage
  ;  (defn my-scroll-f [intersecting?] ...)
  ;  [scroll-sensor my-scroll-f]
  ([callback-f]
   [component (random/generate-keyword) callback-f])

  ([sensor-id callback-f]
   (reagent/lifecycles {:component-did-mount (fn [] (scroll-sensor.helpers/sensor-did-mount-f sensor-id callback-f))
                        :reagent-render      (fn [] [:div {:id sensor-id}])})))
