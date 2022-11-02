
(ns app.common.frontend.item-preview.views
    (:require [elements.api :as elements]))

;; -- Ghost-view components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-preview-ghost-element
  ; @param (keyword) preview-id
  ; @param (map) element-props
  ;
  ; @usage
  ;  [item-editor-ghost-element :my-editor {...}]
  [preview-id _]
  [elements/label {:color     :muted
                   :content   :downloading...
                   :font-size :xs}])

;; -- Error components --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-preview-error-element
  ; @param (keyword) preview-id
  ; @param (map) element-props
  ;  {:error (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-preview-error-content :my-preview {...}]
  [preview-id element-props]
  (let [element-props (merge {:error :the-item-has-been-broken} element-props)]
       [elements/label {:color     :warning
                        :content   (:error element-props)
                        :font-size :xs}]))
