
(ns app.packages.frontend.preview.views
    (:require [app.common.frontend.api                  :as common]
              [app.packages.frontend.preview.prototypes :as preview.prototypes]
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

(defn- package-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)}
  ; @param (namespaced map) package-link
  ;  {:count (integer)
  ;   :id (string)}
  [_ {:keys [disabled?] :as preview-props} {:package/keys [count id]}]
  (let [package-name                @(r/subscribe [:x.db/get-item [:packages :preview/downloaded-items id :name]])
        package-item-number         @(r/subscribe [:x.db/get-item [:packages :preview/downloaded-items id :item-number]])
        package-unit-price          @(r/subscribe [:x.db/get-item [:packages :preview/downloaded-items id :unit-price] 0])
        package-quantity-unit-value @(r/subscribe [:x.db/get-item [:packages :preview/downloaded-items id :quantity-unit :value]])
        package-quantity             {:content package-quantity-unit-value :replacements [count]}]
       [common/data-table {:disabled? disabled?
                           :rows [[          {:content :name        :font-size :xs} {:content package-name                    :color :muted :font-size :xs :placeholder :unnamed-package}]
                                  [          {:content :item-number :font-size :xs} {:content package-item-number             :color :muted :font-size :xs :placeholder "-"}]
                                  (if count [{:content :quantity    :font-size :xs} {:content package-quantity                :color :muted :font-size :xs :placeholder "-"}])
                                  [          {:content :unit-price  :font-size :xs} {:content (str package-unit-price " EUR") :color :muted :font-size :xs :placeholder "-"}]]}]))

(defn- package-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {}
  ; @param (namespaced map) package-link
  ;  {:package/id (string)}
  [_ {:keys [disabled?]} {:package/keys [id]}]
  (let [thumbnail @(r/subscribe [:x.db/get-item [:packages :preview/downloaded-items id :thumbnail]])]
       [storage/media-preview {:disabled?   disabled?
                               :items       [thumbnail]
                               :placeholder :empty-thumbnail
                               :thumbnail   {:height :m :width :xl}}]))

(defn- package-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) package-link
  [preview-id preview-props package-link]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "12px" :align-items "flex-start"}}
        [package-preview-thumbnail preview-id preview-props package-link]
        [package-preview-data      preview-id preview-props package-link]])

(defn- package-preview-static-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) package-link
  [preview-id preview-props {:package/keys [id] :as package-link}]
  ; BUG#9980 (app.products.frontend.preview.views)
  ; BUG#8871 (app.products.frontend.preview.views)
  (if id [item-preview/body (keyword id)
                            {:error-element   [common/error-element {:error :the-content-has-been-broken}]
                             :ghost-element   #'common/item-preview-ghost-element
                             :preview-element [package-preview-element preview-id preview-props package-link]
                             :item-id         id
                             :item-path       [:packages :preview/downloaded-items id]
                             :transfer-id     :packages.preview}]))

(defn- package-preview-sortable-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (integer) item-dex
  ; @param (namespaced map) package-link
  ;  {:package/id (string)}
  ; @param (map) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)
  [preview-id preview-props item-dex {:package/keys [id] :as package-link} {:keys [handle-attributes item-attributes]}]
  [:div (update item-attributes :style merge {:align-items "center" :display "flex" :grid-column-gap "18px"})
        (if @(r/subscribe [:item-preview/data-received? (keyword id)])
             [common/list-item-drag-handle {:drag-attributes handle-attributes}])
        [package-preview-static-body preview-id preview-props package-link]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-preview-static-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list package-link] (conj preview-list [package-preview-static-body preview-id preview-props package-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- package-preview-sortable-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)
  ;   :value-path (vector)}
  [preview-id {:keys [items value-path] :as preview-props}]
  [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}
        [dnd-kit/body preview-id
                      {:common-props     preview-props
                       :items            items
                       :item-id-f        :package/id
                       :item-element     #'package-preview-sortable-body
                       :on-order-changed (fn [_ _ %] (r/dispatch-sync [:x.db/set-item! value-path %]))}]])

(defn- package-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:sortable? (boolean)(opt)}
  [preview-id {:keys [sortable?] :as preview-props}]
  (if sortable? [package-preview-sortable-list preview-id preview-props]
                [package-preview-static-list   preview-id preview-props]))

(defn- package-preview-label
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

(defn- package-preview-placeholder
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

(defn- package-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:indent (map)(opt)
  ;   :items (namespaced maps in vector)(opt)}
  [preview-id {:keys [indent items] :as preview-props}]
  [elements/blank preview-id
                  {:content [:<> [package-preview-label preview-id preview-props]
                                 (if (vector/nonempty? items)
                                     [package-preview-list        preview-id preview-props]
                                     [package-preview-placeholder preview-id preview-props])]
                   :indent  indent}])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :items (namespaced maps in vector)(opt)
  ;    [{:package/count (integer)
  ;      :package/id (string)}
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
  ;  [package-preview {...}]
  ;
  ; @usage
  ;  [package-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [package-preview preview-id preview-props])))
