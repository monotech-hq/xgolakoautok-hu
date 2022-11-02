
(ns app.models.frontend.preview.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-preview.api :as item-preview]
              [mid-fruits.random        :as random]
              [mid-fruits.vector        :as vector]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)}
  ; @param (namespaced map) model-link
  ;  {:count (integer)
  ;   :id (string)}
  [_ {:keys [disabled?] :as preview-props} {:model/keys [count id]}]
  (let [model-name                @(r/subscribe [:db/get-item [:models :preview/downloaded-items id :name]])
        model-product-description @(r/subscribe [:db/get-item [:models :preview/downloaded-items id :product-description] 0])
        model-quantity             {:content :n-pieces :replacements [count]}]
       [common/data-table {:disabled? disabled?
                           :rows [[          {:content :name}                {:content model-name                :color :muted :placeholder :unnamed-model}]
                                  [          {:content :product-description} {:content model-product-description :color :muted :placeholder "-"}]
                                  (if count [{:content :quantity}            {:content model-quantity            :color :muted :placeholder "-"}])]}]))

(defn- model-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {}
  ; @param (namespaced map) model-link
  ;  {:model/id (string)}
  [_ {:keys [disabled?]} {:model/keys [id]}]
  (let [thumbnail @(r/subscribe [:db/get-item [:models :preview/downloaded-items id :thumbnail]])]
       [storage/media-preview {:disabled?   disabled?
                               :items       [thumbnail]
                               :placeholder :empty-thumbnail
                               :thumbnail   {:height :xl
                                             :width  :3xl}}]))

(defn- model-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) model-link
  [preview-id preview-props model-link]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-column-gap "12px" :align-items "flex-start"}}
        [model-preview-thumbnail preview-id preview-props model-link]
        [model-preview-data      preview-id preview-props model-link]])

(defn- model-preview-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) model-link
  ;  {}
  [preview-id preview-props {:model/keys [id] :as model-link}]
  ; BUG#9980 (app.products.frontend.preview.views)
  ; BUG#8871 (app.products.frontend.preview.views)
  (if id [item-preview/body (keyword id)
                            {:error-element   [common/error-element {:error :the-content-has-been-broken}]
                             :ghost-element   #'common/item-preview-ghost-element
                             :preview-element [model-preview-element preview-id preview-props model-link]
                             :item-id         id
                             :item-path       [:models :preview/downloaded-items id]
                             :transfer-id     :models.preview}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list model-link] (conj preview-list [model-preview-body preview-id preview-props model-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- model-preview-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? info-text label]}]
  (if label [elements/label {:content   label
                             :disabled? disabled?
                             :info-text info-text}]))

(defn- model-preview-placeholder
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [_ {:keys [disabled? placeholder]}]
  (if placeholder [elements/label {:color       :muted
                                   :content     placeholder
                                   :disabled?   disabled?
                                   :font-size   :xs
                                   :selectable? true}]))

(defn- model-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:indent (map)(opt)
  ;   :items (namespaced maps in vector)(opt)}
  [preview-id {:keys [indent items] :as preview-props}]
  [elements/blank preview-id
                  {:content [:<> [model-preview-label preview-id preview-props]
                                 (if (vector/nonempty? items)
                                     [model-preview-list        preview-id preview-props]
                                     [model-preview-placeholder preview-id preview-props])]
                   :indent  indent}])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :items (namespaced maps in vector)(opt)
  ;    [{:product/count (integer)
  ;      :product/id (string)}
  ;     {...}]
  ;   :label (metamorphic-content)(opt)
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [model-preview {...}]
  ;
  ; @usage
  ;  [model-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   [model-preview preview-id preview-props]))
