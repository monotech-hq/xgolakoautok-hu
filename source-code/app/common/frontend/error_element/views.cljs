
(ns app.common.frontend.error-element.views
    (:require [app.common.frontend.error-element.prototypes :as error-element.prototypes]
              [elements.api                                 :as elements]
              [mid-fruits.random                            :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- error-message-label
  ; @param (keyword) element-id
  ; @param (map) element-props
  [_ {:keys [error]}]
  [elements/label {:color       :warning
                   :content     error
                   :font-size   :xs
                   :line-height :block}])

(defn- error-element
  ; @param (keyword) element-id
  ; @param (map) element-props
  [element-id element-props]
  [error-message-label element-id element-props])

(defn element
  ; @param (keyword)(opt) element-id
  ; @param (map) element-props
  ;  {:error (metamorphic-content)}
  ;
  ; @usage
  ;  [error-element {...}]
  ;
  ; @usage
  ;  [error-element :my-error-element {...}]
  ([element-props]
   [element (random/generate-keyword) element-props])

  ([element-id element-props]
   (let [element-props (error-element.prototypes/element-props-prototype element-props)]
        [error-element element-id element-props])))
