
(ns app.vehicle-models.frontend.preview.views
    (:require [app.common.frontend.api                  :as common]
              [app.packages.frontend.preview.prototypes :as preview.prototypes]
              [app.storage.frontend.api                 :as storage]
              [elements.api                             :as elements]
              [engines.item-preview.api                 :as item-preview]
              [random.api                        :as random]
              [vector.api                        :as vector]
              [re-frame.api                             :as r]

              ; TEMP
              [plugins.dnd-kit.api :as dnd-kit]))

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
  (let [model-name                @(r/subscribe [:x.db/get-item [:vehicle-models :preview/downloaded-items id :name]])
        model-product-description @(r/subscribe [:x.db/get-item [:vehicle-models :preview/downloaded-items id :product-description] 0])
        model-quantity             {:content :n-pieces :replacements [count]}]
       [common/data-table {:disabled? disabled?
                           :rows [[          {:content :name}                {:content model-name                :color :muted :placeholder :unnamed-vehicle-model}]
                                  [          {:content :product-description} {:content model-product-description :color :muted :placeholder "-"}]
                                  (if count [{:content :quantity}            {:content model-quantity            :color :muted :placeholder "-"}])]}]))

(defn- model-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {}
  ; @param (namespaced map) model-link
  ;  {:model/id (string)}
  [_ {:keys [disabled?]} {:model/keys [id]}]
  (let [thumbnail @(r/subscribe [:x.db/get-item [:vehicle-models :preview/downloaded-items id :thumbnail]])]
       [storage/media-preview {:disabled?   disabled?
                               :items       [thumbnail]
                               :placeholder :empty-thumbnail
                               :thumbnail   {:height :xl :width :3xl}}]))

(defn- model-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) model-link
  [preview-id preview-props model-link]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "12px" :align-items "flex-start"}}
        [model-preview-thumbnail preview-id preview-props model-link]
        [model-preview-data      preview-id preview-props model-link]])

(defn- model-preview-static-body
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
                             :item-path       [:vehicle-models :preview/downloaded-items id]
                             :transfer-id     :vehicle-models.preview}]))

(defn- model-preview-sortable-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (integer) item-dex
  ; @param (namespaced map) model-link
  ;  {:model/id (string)}
  ; @param (map) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)
  [preview-id preview-props item-dex {:model/keys [id] :as model-link} {:keys [handle-attributes item-attributes]}]
  [:div (update item-attributes :style merge {:align-items "center" :display "flex" :grid-column-gap "18px"})
        (if @(r/subscribe [:item-preview/data-received? (keyword id)])
             [common/list-item-drag-handle {:drag-attributes handle-attributes}])
        [model-preview-static-body preview-id preview-props model-link]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-preview-static-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list model-link] (conj preview-list [model-preview-static-body preview-id preview-props model-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- model-preview-sortable-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)
  ;   :value-path (vector)}
  [preview-id {:keys [items value-path] :as preview-props}]
  [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}
        [dnd-kit/body preview-id
                      {:common-props     preview-props
                       :items            items
                       :item-id-f        :vehicle-model/id
                       :item-element     #'model-preview-sortable-body
                       :on-order-changed (fn [_ _ %] (r/dispatch-sync [:x.db/set-item! value-path %]))}]])

(defn- model-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:sortable? (boolean)(opt)}
  [preview-id {:keys [sortable?] :as preview-props}]
  (if sortable? [model-preview-sortable-list preview-id preview-props]
                [model-preview-static-list   preview-id preview-props]))

(defn- model-preview-label
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
                                   :line-height :block
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
  ;   :placeholder (metamorphic-content)(opt)
  ;   :sortable? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)(opt)
  ;    W/ {:sortable? true}}
  ;
  ; @usage
  ;  [model-preview {...}]
  ;
  ; @usage
  ;  [model-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [model-preview preview-id preview-props])))
