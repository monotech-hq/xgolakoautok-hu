
(ns app.contents.frontend.preview.views
    (:require [app.common.frontend.api                  :as common]
              [app.components.frontend.api              :as components]
              [app.contents.frontend.handler.helpers    :as handler.helpers]
              [app.contents.frontend.preview.prototypes :as preview.prototypes]
              [elements.api                             :as elements]
              [engines.item-preview.api                 :as item-preview]
              [random.api                               :as random]
              [re-frame.api                             :as r]
              [vector.api                               :as vector]

              ; TEMP
              [plugins.dnd-kit.api :as dnd-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:color (keyword)
  ;   :disabled? (boolean)(opt)
  ;   :font-size (keyword)
  ;   :font-weight (keyword)
  ;   :max-lines (integer)(opt)
  ;   :style (map)(op)}
  ; @param (namespaced map) content-link
  ;  {:content/id (string)}
  [_ {:keys [color disabled? font-size font-weight max-lines style] :as preview-props} {:content/keys [id]}]
  (let [content-body @(r/subscribe [:x.db/get-item [:contents :preview/downloaded-items id :body]])]
       [elements/text {:color       color
                       :content     (handler.helpers/parse-content-body content-body)
                       :disabled?   disabled?
                       :font-size   font-size
                       :font-weight font-weight
                       :max-lines   max-lines
                       :style       style}]))

(defn- content-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) content-link
  [preview-id preview-props content-link]
  [content-preview-data preview-id preview-props content-link])

(defn- content-preview-static-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) content-link
  ;  {}
  [preview-id preview-props {:content/keys [id] :as content-link}]
  ; BUG#9980 (app.products.frontend.preview.views)
  ; BUG#8871 (app.products.frontend.preview.views)
  (if id [item-preview/body (keyword id)
                            {:ghost-element   #'common/item-preview-ghost-element
                             :error-element   [common/error-element {:error :the-content-has-been-broken}]
                             :preview-element [content-preview-element preview-id preview-props content-link]
                             :item-id         id
                             :item-path       [:contents :preview/downloaded-items id]
                             :transfer-id     :contents.preview}]))

(defn- content-preview-sortable-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (integer) item-dex
  ; @param (namespaced map) content-link
  ;  {:content/id (string)}
  ; @param (map) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)}
  [preview-id preview-props item-dex {:content/keys [id] :as content-link} {:keys [handle-attributes item-attributes]}]
  [:div (update item-attributes :style merge {:align-items "center" :display "flex" :grid-column-gap "18px"})
        (if @(r/subscribe [:item-preview/data-received? (keyword id)])
             [components/list-item-drag-handle {:drag-attributes handle-attributes}])
        [content-preview-static-body preview-id preview-props content-link]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-preview-static-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list content-link] (conj preview-list [content-preview-static-body preview-id preview-props content-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- content-preview-sortable-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)
  ;   :value-path (vector)}
  [preview-id {:keys [items value-path] :as preview-props}]
  [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}
        [dnd-kit/body preview-id
                      {:common-props     preview-props
                       :items            items
                       :item-id-f        :content/id
                       :item-element     #'content-preview-sortable-body
                       :on-order-changed (fn [_ _ %] (r/dispatch-sync [:x.db/set-item! value-path %]))}]])

(defn- content-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:sortable? (boolean)(opt)}
  [preview-id {:keys [sortable?] :as preview-props}]
  (if sortable? [content-preview-sortable-list preview-id preview-props]
                [content-preview-static-list   preview-id preview-props]))

(defn- content-preview-label
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

(defn- content-preview-placeholder
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

(defn- content-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:indent (map)(opt)
  ;   :items (namespaced maps in vector)(opt)}
  [preview-id {:keys [indent items] :as preview-props}]
  [elements/blank preview-id
                  {:content [:<> [content-preview-label preview-id preview-props]
                                 (if (vector/nonempty?            items)
                                     [content-preview-list        preview-id preview-props]
                                     [content-preview-placeholder preview-id preview-props])]
                   :indent  indent}])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:color (keyword)(opt)
  ;    :default, :highlight, :inherit, :muted
  ;    Default: :default
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  ;    Default: :s
  ;   :font-weight (keyword)(opt)
  ;    :bold, :extra-bold, :inherit, :normal
  ;    Default: :normal
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :items (namespaced maps in vector)(opt)
  ;    [{:content/id (string)}
  ;     {...}]
  ;   :label (metamorphic-content)(opt)
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;   :max-lines (integer)(opt)
  ;   :multi-select? (boolean)(opt)
  ;    Default: false
  ;   :placeholder (metamorphic-content)(opt)
  ;   :sortable? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :value-path (vector)(opt)
  ;    W/ {:sortable? true}}
  ;
  ; @usage
  ;  [content-preview {...}]
  ;
  ; @usage
  ;  [content-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [content-preview preview-id preview-props])))
