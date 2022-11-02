
(ns app.common.frontend.list-item-marker.views
    (:require [elements.api    :as elements]
              [mid-fruits.math :as math]
              [mid-fruits.css  :as css]
              [re-frame.api    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-marker-progress
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) cell-props
  ;  {:icon (keyword)
  ;   :progress (percent)
  ;   :progress-duration (ms)
  ;   :style (map)(opt)}
  [_ _ {:keys [progress progress-duration]}]
  (let [percent             (math/percent-result 69.11 progress)
        stroke-dasharray    (str percent" "(- 100 percent))
        transition-duration (css/ms progress-duration)
        transition          (if progress (str "stroke-dasharray " transition-duration " linear"))]
       [:svg {:view-box "0 0 24 24" :style {:position "absolute" :width "24px" :height "24px"}}
             [:circle {:style {:width "24px" :height "24px" :fill "transparent" :transition transition}
                       :stroke "var( --border-color-primary )" :stroke-dasharray stroke-dasharray
                       :stroke-width "2" :cx "12" :cy "12" :r "11"}]]))

(defn- list-item-marker
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) marker-props
  ;  {:icon (keyword)
  ;   :style (map)(opt)}
  [lister-id item-dex {:keys [icon style] :as marker-props}]
  [:div [list-item-marker-progress lister-id item-dex marker-props]
        [elements/icon {:icon icon :indent {:right :xs} :size :s :style style}]])

(defn element
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (map) marker-props
  ;  {:icon (keyword)
  ;   :progress (percent)(opt)
  ;   :progress-duration (ms)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [list-item-marker :my-lister 0 {...}]
  [lister-id item-dex marker-props]
  [list-item-marker lister-id item-dex marker-props])
