
(ns app.components.frontend.list-item-marker.views
    (:require [css.api           :as css]
              [elements.api      :as elements]
              [math.api          :as math]
              [mid-fruits.random :as random]
              [re-frame.api      :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-marker-progress
  ; @param (keyword) marker-id
  ; @param (map) cell-props
  ;  {:icon (keyword)
  ;   :progress (percent)
  ;   :progress-duration (ms)
  ;   :style (map)(opt)}
  [_ {:keys [progress progress-duration]}]
  ; W:  24px
  ; H:  24px
  ; Do: W                 = 24px
  ; Di: W - 2stroke-width = 20px
  ; Ro: Do / 2            = 12px
  ; Ri: Di / 2            = 10px
  ; Rc: (Do + Di) / 2     = 11px
  ; CIRCUM: 2Rc * Pi      = 69.11px
  (let [percent-result      (math/percent-result 69.11        progress)
        percent-rem         (math/percent-result 69.11 (- 100 progress))
        stroke-dasharray    (str percent-result" "percent-rem)
        transition-duration (css/ms progress-duration)
        transition          (if progress (str "stroke-dasharray " transition-duration " linear"))]
       [:svg {:view-box "0 0 24 24" :style {:position "absolute" :width "24px" :height "24px"}}
             [:circle {:style {:fill "transparent" :transition transition}
                       :stroke "var( --border-color-primary )" :stroke-dasharray stroke-dasharray
                       :stroke-width "2" :cx "12" :cy "12" :r "11"}]]))

(defn- list-item-marker-body
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ;  {:icon (keyword)}
  [marker-id {:keys [icon] :as marker-props}]
  [:div [list-item-marker-progress marker-id marker-props]
        [elements/icon {:icon icon :indent {:right :xs} :size :s}]])

(defn- list-item-marker
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}
  [marker-id {:keys [class disabled? indent style] :as marker-props}]
  [elements/blank marker-id
                  {:class     class
                   :disabled? disabled?
                   :indent    indent
                   :content   [list-item-marker-body marker-id marker-props]
                   :style     style}])

(defn component
  ; @param (keyword)(opt) marker-id
  ; @param (map) marker-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :icon (keyword)
  ;   :indent (map)(opt)
  ;   :progress (percent)(opt)
  ;   :progress-duration (ms)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [list-item-marker {...}]
  ;
  ; @usage
  ;  [list-item-marker :my-marker {...}]
  ([marker-props]
   [component (random/generate-keyword) marker-props])

  ([marker-id marker-props]
   [list-item-marker marker-id marker-props]))
