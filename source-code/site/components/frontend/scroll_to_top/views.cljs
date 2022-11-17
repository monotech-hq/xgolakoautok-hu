
(ns site.components.frontend.scroll-to-top.views
    (:require [elements.api                                   :as elements]
              [random.api                                     :as random]
              [re-frame.api                                   :as r]
              [site.components.frontend.scroll-sensor.views   :as scroll-sensor.views]
              [site.components.frontend.scroll-to-top.helpers :as scroll-to-top.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- scroll-to-top
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:color (string)(opt)
  ;   :style (map)(opt)}
  [_ {:keys [color style]}]
  [:<> [scroll-sensor.views/component ::scroll-sensor {:callback-f scroll-to-top.helpers/scroll-f
                                                       :style {:left "0" :position "absolute" :bottom "0"}}]
       [:div {:id :mt-scroll-to-top :style style :on-click #(r/dispatch {:fx [:x.environment/reset-scroll-y!]})}
             [elements/icon ::scroll-to-top
                            {:color (or color "white")
                             :icon :arrow_upward
                             :size :xxl}]]])

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ;  {:color (string)(opt)
  ;    Default: "white"
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [scroll-to-top {...}]
  ;
  ; @usage
  ;  [scroll-to-top :my-scroll-to-top {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [scroll-to-top component-id component-props]))
