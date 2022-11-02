
(ns app.types.frontend.preview.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-preview.api :as item-preview]
              [mid-fruits.random        :as random]
              [mid-fruits.vector        :as vector]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)}
  ; @param (namespaced map) type-link
  ;  {:count (integer)
  ;   :id (string)}
  [_ {:keys [disabled?] :as preview-props} {:type/keys [count id]}]
  (let [type-name               @(r/subscribe [:db/get-item [:types :preview/downloaded-items id :name]])
        type-outer-dimensions   @(r/subscribe [:types.preview/get-type-outer-dimensions       id])
        type-inner-dimensions   @(r/subscribe [:types.preview/get-type-inner-dimensions       id])
        type-manufacturer-price @(r/subscribe [:db/get-item [:types :preview/downloaded-items id :manufacturer-price]])
        type-transport-cost     @(r/subscribe [:db/get-item [:types :preview/downloaded-items id :transport-cost]])
        type-price-margin       @(r/subscribe [:db/get-item [:types :preview/downloaded-items id :price-margin]])
        type-dealer-rebate      @(r/subscribe [:db/get-item [:types :preview/downloaded-items id :dealer-rebate]])
        type-quantity            {:content :n-pieces :replacements [count]}]
       [common/data-table {:disabled? disabled?
                           :rows [[          {:content :name}                 {:content type-name                                         :color :muted :placeholder :unnamed-type}]
                                  (if count [{:content :quantity}             {:content type-quantity                                     :color :muted :placeholder "-"}])
                                  [          {:content :manufacturer-price}   {:content {:content type-manufacturer-price :suffix " EUR"} :color :muted :placeholder "-"}]
                                  [          {:content :transport-cost}       {:content {:content type-transport-cost     :suffix " EUR"} :color :muted :placeholder "-"}]
                                  [          {:content :price-margin}         {:content {:content type-price-margin       :suffix " %"}   :color :muted :placeholder "-"}]
                                  [          {:content :dealer-rebate}        {:content {:content type-dealer-rebate      :suffix " %"}   :color :muted :placeholder "-"}]
                                  [          {:content :outer-dimensions-wlh} {:content type-outer-dimensions                             :color :muted :placeholder "-"}]
                                  [          {:content :inner-dimensions-wlh} {:content type-inner-dimensions                             :color :muted :placeholder "-"}]]}]))

(defn- type-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) type-link
  [preview-id preview-props type-link]
  [type-preview-data preview-id preview-props type-link])

(defn- type-preview-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) type-link
  ;  {}
  [preview-id preview-props {:type/keys [id] :as type-link}]
  ; BUG#9980 (app.products.frontend.preview.views)
  ; BUG#8871 (app.products.frontend.preview.views)
  (if id [item-preview/body (keyword id)
                            {:error-element   [common/error-element {:error :the-content-has-been-broken}]
                             :ghost-element   #'common/item-preview-ghost-element
                             :preview-element [type-preview-element preview-id preview-props type-link]
                             :item-id         id
                             :item-path       [:types :preview/downloaded-items id]
                             :transfer-id     :types.preview}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list type-link] (conj preview-list [type-preview-body preview-id preview-props type-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- type-preview-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? info-text label]}]
  (if label [elements/label {:content   label
                             :disabled? disabled?
                             :info-text info-text}]))

(defn- type-preview-placeholder
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

(defn- type-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:indent (map)(opt)
  ;   :items (namespaced maps in vector)(opt)}
  [preview-id {:keys [indent items] :as preview-props}]
  [elements/blank preview-id
                  {:content [:<> [type-preview-label preview-id preview-props]
                                 (if (vector/nonempty? items)
                                     [type-preview-list        preview-id preview-props]
                                     [type-preview-placeholder preview-id preview-props])]
                   :indent  indent}])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :items (namespaced maps in vector)(opt)
  ;    [{:type/count (integer)
  ;      :type/id (string)}
  ;     {...}]
  ;   :label (metamorphic-content)(opt)
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [type-preview {...}]
  ;
  ; @usage
  ;  [type-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   [type-preview preview-id preview-props]))
