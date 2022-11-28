
(ns app.components.frontend.list-item-marker.views
    (:require [css.api      :as css]
              [elements.api :as elements]
              [math.api     :as math]
              [random.api   :as random]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-marker
  ; @param (keyword) marker-id
  ; @param (map) marker-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}
  [marker-id {:keys [class disabled? icon indent style] :as marker-props}]
  [elements/blank marker-id
                  {:class     class
                   :disabled? disabled?
                   :indent    indent
                   :content   [elements/icon {:icon icon :indent {:right :xs} :size :s}]
                   :style     style}])

(defn component
  ; @param (keyword)(opt) marker-id
  ; @param (map) marker-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :icon (keyword)
  ;   :indent (map)(opt)
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
