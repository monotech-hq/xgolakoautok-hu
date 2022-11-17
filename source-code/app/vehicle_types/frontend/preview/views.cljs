
(ns app.vehicle-types.frontend.preview.views
    (:require [app.common.frontend.api                       :as common]
              [app.storage.frontend.api                      :as storage]
              [app.vehicle-types.frontend.preview.prototypes :as preview.prototypes]
              [elements.api                                  :as elements]
              [engines.item-preview.api                      :as item-preview]
              [random.api                                    :as random]
              [re-frame.api                                  :as r]
              [vector.api                                    :as vector]

              ; TEMP
              [plugins.dnd-kit.api :as dnd-kit]))

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
  (let [type-name               @(r/subscribe [:x.db/get-item [:vehicle-types :preview/downloaded-items id :name]])
        type-outer-dimensions   @(r/subscribe [:vehicle-types.preview/get-type-outer-dimensions       id])
        type-inner-dimensions   @(r/subscribe [:vehicle-types.preview/get-type-inner-dimensions       id])
        type-manufacturer-price @(r/subscribe [:x.db/get-item [:vehicle-types :preview/downloaded-items id :manufacturer-price]])
        type-transport-cost     @(r/subscribe [:x.db/get-item [:vehicle-types :preview/downloaded-items id :transport-cost]])
        type-price-margin       @(r/subscribe [:x.db/get-item [:vehicle-types :preview/downloaded-items id :price-margin]])
        type-dealer-rebate      @(r/subscribe [:x.db/get-item [:vehicle-types :preview/downloaded-items id :dealer-rebate]])
        type-quantity            {:content :n-pieces :replacements [count]}]
       [common/data-table {:disabled? disabled?
                           :rows [[          {:content :name}                 {:content type-name                                         :color :muted :placeholder :unnamed-vehicle-type}]
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

(defn- type-preview-static-body
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
                             :item-path       [:vehicle-types :preview/downloaded-items id]
                             :transfer-id     :vehicle-types.preview}]))

(defn- type-preview-sortable-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (integer) item-dex
  ; @param (namespaced map) type-link
  ;  {:type/id (string)}
  ; @param (map) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)
  [preview-id preview-props item-dex {:type/keys [id] :as type-link} {:keys [handle-attributes item-attributes]}]
  [:div (update item-attributes :style merge {:align-items "center" :display "flex" :grid-column-gap "18px"})
        (if @(r/subscribe [:item-preview/data-received? (keyword id)])
             [common/list-item-drag-handle {:drag-attributes handle-attributes}])
        [type-preview-static-body preview-id preview-props type-link]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-preview-static-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list type-link] (conj preview-list [type-preview-static-body preview-id preview-props type-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- type-preview-sortable-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)
  ;   :value-path (vector)}
  [preview-id {:keys [items value-path] :as preview-props}]
  [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}
        [dnd-kit/body preview-id
                      {:common-props     preview-props
                       :items            items
                       :item-id-f        :type/id
                       :item-element     #'type-preview-sortable-body
                       :on-order-changed (fn [_ _ %] (r/dispatch-sync [:x.db/set-item! value-path %]))}]])

(defn- type-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:sortable? (boolean)(opt)}
  [preview-id {:keys [sortable?] :as preview-props}]
  (if sortable? [type-preview-sortable-list preview-id preview-props]
                [type-preview-static-list   preview-id preview-props]))

(defn- type-preview-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? info-text label]}]
  (if label [elements/label {:content     label
                             :disabled?   disabled?
                             :info-text   info-text
                             :line-height :block}]))

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
                                   :line-height :block
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
  ;   :placeholder (metamorphic-content)(opt)
  ;   :sortable? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)(opt)
  ;    W/ {:sortable? true}}
  ;
  ; @usage
  ;  [type-preview {...}]
  ;
  ; @usage
  ;  [type-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [type-preview preview-id preview-props])))
