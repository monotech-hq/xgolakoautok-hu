
(ns app.components.frontend.surface-description.views
    (:require [app.components.frontend.surface-description.prototypes :as surface-description.prototypes]
              [elements.api                                           :as elements]
              [random.api                                             :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-description
  ; @param (keyword) description-id
  ; @param (map) description-props
  ;  {:content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;   :horizontal-align (keyword)
  ;   :indent (map)}
  [description-id {:keys [content disabled? horizontal-align indent]}]
  [elements/label ::surface-description
                  {:color            :muted
                   :content          content
                   :disabled?        disabled?
                   :font-size        :xxs
                   :line-height      :block
                   :horizontal-align horizontal-align
                   :indent           indent}])

(defn component
  ; @param (keyword)(opt) description-id
  ; @param (map) description-props
  ;  {:content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;   :indent (map)(opt)
  ;    Default: {:horizontal :xxs}}
  ;
  ; @usage
  ;  [surface-description {...}]
  ;
  ; @usage
  ;  [surface-description :my-surface-description {...}]
  ([description-props]
   [component (random/generate-keyword) description-props])

  ([description-id description-props]
   (let [description-props (surface-description.prototypes/description-props-prototype description-props)]
        [surface-description description-id description-props])))
