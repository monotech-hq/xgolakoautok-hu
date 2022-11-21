
(ns app.components.frontend.error-content.views
    (:require [app.components.frontend.error-content.prototypes :as error-content.prototypes]
              [elements.api                                     :as elements]
              [random.api                                       :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- error-occured-label
  ; @param (keyword) content-id
  ; @param (map) content-props
  [_ _]
  [elements/label {:color            :warning
                   :content          :an-error-occured
                   :font-size        :m
                   :horizontal-align :center
                   :indent           {:top :xxl}
                   :line-height      :block}])

(defn- error-message-label
  ; @param (keyword) content-id
  ; @param (map) content-props
  [_ {:keys [error]}]
  [elements/label {:color            :muted
                   :content          error
                   :horizontal-align :center
                   :indent           {:bottom :xxl}
                   :line-height      :block}])

(defn- error-content
  ; @param (keyword) content-id
  ; @param (map) content-props
  [content-id content-props]
  [:<> [error-occured-label content-id content-props]
       [error-message-label content-id content-props]])

(defn component
  ; @param (keyword)(opt) content-id
  ; @param (map) content-props
  ;  {:error (metamorphic-content)}
  ;
  ; @usage
  ;  [error-content {...}]
  ;
  ; @usage
  ;  [error-content :my-error-content {...}]
  ([content-props]
   [component (random/generate-keyword) content-props])

  ([content-id content-props]
   (let [];content-props (error-content.prototypes/content-props-prototype content-props)
        [error-content content-id content-props])))
