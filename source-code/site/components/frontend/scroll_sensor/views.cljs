
(ns site.components.frontend.scroll-sensor.views
    (:require [hiccup.api                                     :as hiccup]
              [random.api                                     :as random]
              [reagent.api                                    :as reagent]
              [site.components.frontend.scroll-sensor.helpers :as scroll-sensor.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) sensor-id
  ; @param (map) sensor-props
  ;  {:callback-f (function)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [scroll-sensor {...}]
  ;
  ; @usage
  ;  [scroll-sensor :my-sensor {...}]
  ;
  ; @usage
  ;  (defn my-scroll-f [intersecting?] ...)
  ;  [scroll-sensor {:callback-f my-scroll-f}]
  ([sensor-props]
   [component (random/generate-keyword) sensor-props])

  ([sensor-id {:keys [callback-f style]}]
   (reagent/lifecycles {:component-did-mount (fn [] (scroll-sensor.helpers/sensor-did-mount-f sensor-id callback-f))
                        :reagent-render      (fn [] [:div {:id (hiccup/value sensor-id) :style style}])})))
