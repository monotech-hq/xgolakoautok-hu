
(ns app.services.frontend.preview.views
    (:require [app.common.frontend.api                  :as common]
              [app.components.frontend.api              :as components]
              [app.services.frontend.preview.prototypes :as preview.prototypes]
              [app.storage.frontend.api                 :as storage]
              [elements.api                             :as elements]
              [engines.item-preview.api                 :as item-preview]
              [random.api                               :as random]
              [re-frame.api                             :as r]
              [vector.api                               :as vector]

              ; TEMP
              [plugins.dnd-kit.api :as dnd-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)}
  ; @param (namespaced map) service-link
  ;  {:count (integer)
  ;   :id (string)}
  [_ {:keys [disabled?] :as preview-props} {:service/keys [count id]}]
  (let [service-name                @(r/subscribe [:x.db/get-item [:services :preview/downloaded-items id :name]])
        service-item-number         @(r/subscribe [:x.db/get-item [:services :preview/downloaded-items id :item-number]])
        service-unit-price          @(r/subscribe [:x.db/get-item [:services :preview/downloaded-items id :unit-price] 0])
        service-quantity-unit-value @(r/subscribe [:x.db/get-item [:services :preview/downloaded-items id :quantity-unit :value]])
        service-quantity             {:content service-quantity-unit-value :replacements [count]}]
       [common/data-table {:disabled? disabled?
                           :rows [[          {:content :name        :font-size :xs} {:content service-name                    :color :muted :font-size :xs :placeholder :unnamed-service}]
                                  [          {:content :item-number :font-size :xs} {:content service-item-number             :color :muted :font-size :xs :placeholder "-"}]
                                  (if count [{:content :quantity    :font-size :xs} {:content service-quantity                :color :muted :font-size :xs :placeholder "-"}])
                                  [          {:content :unit-price  :font-size :xs} {:content (str service-unit-price " EUR") :color :muted :font-size :xs :placeholder "-"}]]}]))

(defn- service-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {}
  ; @param (namespaced map) service-link
  ;  {:service/id (string)}
  [_ {:keys [disabled?]} {:service/keys [id]}]
  (let [thumbnail @(r/subscribe [:x.db/get-item [:services :preview/downloaded-items id :thumbnail]])]
       [storage/media-preview {:disabled?   disabled?
                               :items       [thumbnail]
                               :placeholder :empty-thumbnail
                               :thumbnail   {:height :m
                                             :width  :xl}}]))

(defn- service-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) service-link
  ;  {}
  [preview-id preview-props]
  [service-preview-data preview-id preview-props])

(defn- service-preview-static-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) service-link
  ;  {}
  [preview-id preview-props {:service/keys [id] :as service-link}]
  ; BUG#9980 (app.products.frontend.preview.views)
  ; BUG#8871 (app.products.frontend.preview.views)
  (if id [item-preview/body (keyword id)
                            {:error-element   [common/error-element {:error :the-content-has-been-broken}]
                             :ghost-element   #'common/item-preview-ghost-element
                             :preview-element [service-preview-element preview-id preview-props]
                             :item-id         id
                             :item-path       [:services :preview/downloaded-items id]
                             :transfer-id     :services.preview}]))

(defn- service-preview-sortable-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (integer) item-dex
  ; @param (namespaced map) service-link
  ;  {:service/id (string)}
  ; @param (map) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)
  [preview-id preview-props item-dex {:service/keys [id] :as service-link} {:keys [handle-attributes item-attributes]}]
  [:div (update item-attributes :style merge {:align-items "center" :display "flex" :grid-column-gap "18px"})
        (if @(r/subscribe [:item-preview/data-received? (keyword id)])
             [components/list-item-drag-handle {:drag-attributes handle-attributes}])
        [service-preview-static-body preview-id preview-props service-link]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-preview-static-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list service-link] (conj preview-list [service-preview-static-body preview-id preview-props service-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- service-preview-sortable-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)
  ;   :value-path (vector)}
  [preview-id {:keys [items value-path] :as preview-props}]
  [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}
        [dnd-kit/body preview-id
                      {:common-props     preview-props
                       :items            items
                       :item-id-f        :service/id
                       :item-element     #'service-preview-sortable-body
                       :on-order-changed (fn [_ _ %] (r/dispatch-sync [:x.db/set-item! value-path %]))}]])

(defn- service-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:sortable? (boolean)(opt)}
  [preview-id {:keys [sortable?] :as preview-props}]
  (if sortable? [service-preview-sortable-list preview-id preview-props]
                [service-preview-static-list   preview-id preview-props]))

(defn- service-preview-label
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

(defn- service-preview-placeholder
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

(defn- service-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:indent (map)(opt)
  ;   :items (namespaced maps in vector)(opt)}
  [preview-id {:keys [indent items] :as preview-props}]
  [elements/blank preview-id
                  {:content [:<> [service-preview-label preview-id preview-props]
                                 (if (vector/nonempty?            items)
                                     [service-preview-list        preview-id preview-props]
                                     [service-preview-placeholder preview-id preview-props])]
                   :indent  indent}])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :items (namespaced maps in vector)(opt)
  ;    [{:service/count (integer)
  ;      :service/id (string)}
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
  ;  [service-preview {...}]
  ;
  ; @usage
  ;  [service-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [service-preview preview-id preview-props])))
