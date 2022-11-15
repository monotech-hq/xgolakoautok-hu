
(ns app.components.frontend.list-item-thumbnail.views
    (:require [elements.api      :as elements]
              [mid-fruits.random :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-thumbnail-body
  ; @param (keyword)(opt) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;   :thumbnail (string)(opt)}
  [_ {:keys [icon icon-family thumbnail]}]
  (cond icon-family [elements/icon      {:icon icon :icon-family icon-family :indent {:horizontal :m :vertical :xl}}]
        icon        [elements/icon      {:icon icon                          :indent {:horizontal :m :vertical :xl}}]
        thumbnail   [elements/thumbnail {:border-radius :s :height :s :indent {:horizontal :xxs :vertical :xs} :uri thumbnail :width :l}]
        :return     [elements/thumbnail {:border-radius :s :height :s :indent {:horizontal :xxs :vertical :xs}                :width :l}]))

(defn- list-item-thumbnail
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}
  [thumbnail-id {:keys [class disabled? indent style] :as thumbnail-props}]
  [elements/blank thumbnail-id
                  {:class     class
                   :disabled? disabled?
                   :indent    indent
                   :content   [list-item-thumbnail-body thumbnail-id thumbnail-props]
                   :style     style}])

(defn component
  ; @param (keyword)(opt) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}
  ;   :thumbnail (string)(opt)}
  ;
  ; @usage
  ;  [list-item-thumbnail {...}]
  ;
  ; @usage
  ;  [list-item-thumbnail :my-thumbnail {...}]
  ;
  ; @usage
  ;  [list-item-thumbnail {:thumbnail "/my-thumbnail.png"}]
  ;
  ; @usage
  ;  [list-item-thumbnail {:icon :people}]
  ([thumbnail-props]
   [component (random/generate-keyword) thumbnail-props])

  ([thumbnail-id thumbnail-props]
   [list-item-thumbnail thumbnail-id thumbnail-props]))
