
(ns site.components.frontend.follow-us.views
    (:require [random.api                                   :as random]
              [site.components.frontend.follow-us.helpers   :as follow-us.helpers]
              [site.components.frontend.scroll-sensor.views :as scroll-sensor.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- follow-us
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:color (string)(opt)
  ;   :links (map)
  ;   :style (map)(opt)}
  [_ {{:keys [facebook instagram linkedin]} :links :keys [color style]}]
  [:<> [scroll-sensor.views/component ::scroll-sensor {:callback-f follow-us.helpers/scroll-f
                                                       :style {:left "0" :position "absolute" :top "0"}}]
       [:div {:id :mt-follow-us :style style}
             (if facebook  [:a {:href facebook  :target "_blank"} [:i.fab.fa-facebook  {:style {:color (or color "white")}}]])
             (if instagram [:a {:href instagram :target "_blank"} [:i.fab.fa-instagram {:style {:color (or color "white")}}]])
             (if linkedin  [:a {:href linkedin  :target "_blank"} [:i.fab.fa-linkedin  {:style {:color (or color "white")}}]])]])

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ;  {:color (string)(opt)
  ;    Default: "white"
  ;   :links (map)
  ;    {:facebook (string)
  ;     :instagram (string)
  ;     :linkedin (string)}
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [follow-us {...}]
  ;
  ; @usage
  ;  [follow-us :my-follow-us {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [follow-us component-id component-props]))
