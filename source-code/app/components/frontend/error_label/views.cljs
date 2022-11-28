
(ns app.components.frontend.error-label.views
    (:require [app.components.frontend.error-label.prototypes :as error-label.prototypes]
              [elements.api                                   :as elements]
              [random.api                                     :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- error-message-label
  ; @param (keyword) label-id
  ; @param (map) label-props
  [_ {:keys [error]}]
  [elements/label {:color       :warning
                   :content     error
                   :font-size   :xs
                   :line-height :block}])

(defn- error-label
  ; @param (keyword) label-id
  ; @param (map) label-props
  [label-id label-props]
  [error-message-label label-id label-props])

(defn component
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ;  {:error (metamorphic-content)}
  ;
  ; @usage
  ;  [error-label {...}]
  ;
  ; @usage
  ;  [error-label :my-error-label {...}]
  ([label-props]
   [component (random/generate-keyword) label-props])

  ([label-id label-props]
   (let [];label-props (error-label.prototypes/label-props-prototype label-props)
        [error-label label-id label-props])))
