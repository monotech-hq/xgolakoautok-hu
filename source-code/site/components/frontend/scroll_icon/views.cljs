
(ns site.components.frontend.scroll-icon.views
    (:require [random.api                                   :as random]
              [site.components.frontend.scroll-icon.helpers :as scroll-icon.helpers]
              [site.components.frontend.scroll-sensor.views :as scroll-sensor.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- scroll-icon
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:color (string)(opt)
  ;   :style (map)(opt)}
  [_ {:keys [color style]}]
  [:<> [scroll-sensor.views/component ::scroll-sensor {:callback-f scroll-icon.helpers/scroll-f
                                                       :style {:left 2 :position "absolute" :top "0"}}]
       [:div {:id :mt-scroll-icon :style style}
             [:div {:id :mt-scroll-icon--body :style {"--icon-color" (or color "white")}}]]])

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ;  {:color (string)(opt)
  ;    Default: "white"
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [scroll-icon {...}]
  ;
  ; @usage
  ;  [scroll-icon :my-scroll-icon {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [scroll-icon component-id component-props]))
