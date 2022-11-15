
(ns app.components.frontend.user-avatar.views
    (:require [app.components.frontend.user-avatar.prototypes :as user-avatar.prototypes]
              [css.api                                        :as css]
              [elements.api                                   :as elements]
              [math.api                                       :as math]
              [mid-fruits.random                              :as random]
              [mid-fruits.string                              :as string]
              [mid-fruits.vector                              :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- user-avatar-color
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ; @param (integer) dex
  ; @param (string) color
  [_ {:keys [colors]} dex color]
  ; W:  60px
  ; H:  120px
  ; Do: W                 = 60px
  ; Di: W - 2stroke-width = 56px
  ; Ro: Do / 2            = 30px
  ; Ri: Di / 2            = 28px
  ; Rc: (Do + Di) / 2     = 29px
  ; CIRCUM: 2Rc * Pi      = 185.35
  (let [percent          (/ 100 (count colors))
        percent-result   (math/percent-result 185.35        percent)
        percent-rem      (math/percent-result 185.35 (- 100 percent))
        stroke-dasharray (str percent-result" "percent-rem)
        rotation-angle   (+ 30 (* dex (/ 360 (count colors))))]
       [:circle {:style {:fill :transparent :transform-origin :center :transform (css/rotate-z rotation-angle)}
                 :stroke color :stroke-dasharray stroke-dasharray :stroke-width "2" :cx "30" :cy "30" :r "29"}]))

(defn- user-avatar-colors
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ;  {:colors (strings in vector)
  ;   :first-name (string)(opt)
  ;   :last-name (string)(opt)}
  [avatar-id {:keys [colors] :as avatar-props}]
  [:svg {:view-box "0 0 60 60" :style {:position "absolute" :width "60px" :height "60px" :top "0"}}
        (letfn [(f [colors dex color] (conj colors [user-avatar-color avatar-id avatar-props dex color]))]
               (reduce-kv f [:<>] colors))])

(defn- user-avatar-icon
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  [_ _]
  [:div {:style {:height "100%" :width "100%" :background-color "var( --background-color-highlight )"
                 :border-radius "50%" :display "flex" :align-items "center" :justify-content "center"}}
        [elements/icon {:icon :person}]])

(defn- user-avatar-initials
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ;  {:initials (string)}
  [_ {:keys [initials]}]
  [:div {:style {:height "100%" :width "100%" :background-color "var( --background-color-highlight )"
                 :border-radius "50%" :display "flex" :align-items "center" :justify-content "center"}}
        [elements/label {:content     initials
                         :font-size   :s
                         :font-weight :extra-bold}]])

(defn- user-avatar-body
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ;  {:colors (strings in map)(opt)
  ;   :initials (string)(opt)}
  [avatar-id {:keys [colors initials] :as avatar-props}]
  [:div {:style {:width "60px" :height "60px"}}
        (if (string/nonempty? initials) [user-avatar-initials avatar-id avatar-props]
                                        [user-avatar-icon     avatar-id avatar-props])
        (if (vector/nonempty? colors)   [user-avatar-colors   avatar-id avatar-props])])

(defn- user-avatar
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}
  [avatar-id {:keys [class disabled? indent style] :as avatar-props}]
  [elements/blank avatar-id
                  {:class     class
                   :disabled? disabled?
                   :indent    indent
                   :content   [user-avatar-body avatar-id avatar-props]
                   :style     style}])

(defn component
  ; @param (keyword)(opt) avatar-id
  ; @param (map) avatar-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :colors (strings in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :first-name (string)(opt)
  ;   :indent (map)(opt)
  ;   :last-name (string)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [user-avatar {...}]
  ;
  ; @usage
  ;  [user-avatar :my-user-avatar {...}]
  ([avatar-props]
   [component (random/generate-keyword) avatar-props])

  ([avatar-id avatar-props]
   (let [avatar-props (user-avatar.prototypes/avatar-props-prototype avatar-props)]
        [user-avatar avatar-id avatar-props])))
