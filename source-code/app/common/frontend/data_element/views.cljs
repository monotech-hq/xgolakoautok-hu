
(ns app.common.frontend.data-element.views
    (:require [app.common.frontend.data-element.prototypes :as data-element.prototypes]
              [elements.api                                :as elements]
              [mid-fruits.random                           :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-element-label
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:disabled? (boolean)(opt)
  ;   :font-size (keyword)
  ;   :helper (metamorphic-content)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? font-size helper info-text label]}]
  (if label [elements/label {:content             label
                             :disabled?           disabled?
                             :font-size           font-size
                             :helper              helper
                             :horizontal-position :left
                             :info-text           info-text
                             :selectable?         false}]))

(defn- data-element-value
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:disabled? (boolean)(opt)
  ;   :font-size (keyword)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :value (metamorphic-content)(opt)}
  [_ {:keys [disabled? font-size placeholder value]}]
  [elements/text {:color               :muted
                  :content             value
                  :disabled?           disabled?
                  :font-size           font-size
                  :horizontal-position :left
                  :placeholder         placeholder
                  :selectable?         true}])

(defn- data-element
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:indent (map)(opt)}
  [element-id {:keys [indent] :as element-props}]
  [elements/blank {:indent  indent
                   :content [:<> [data-element-label element-id element-props]
                                 [data-element-value element-id element-props]]}])

(defn element
  ; @param (keyword)(opt) element-id
  ; @param (map) element-props
  ;  {:disabled? (boolean)(opt)}
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :value (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [data-element {...}]
  ;
  ; @usage
  ;  [data-element :my-data-element {...}]
  ([element-props]
   [element (random/generate-keyword) element-props])

  ([element-id element-props]
   (let [element-props (data-element.prototypes/element-props-prototype element-props)]
        [data-element element-id element-props])))
