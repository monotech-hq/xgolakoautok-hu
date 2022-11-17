
(ns app.price-quote-templates.frontend.preview.views
    (:require [app.common.frontend.api                               :as common]
              [app.price-quote-templates.frontend.preview.prototypes :as preview.prototypes]
              [app.storage.frontend.api                              :as storage]
              [elements.api                                          :as elements]
              [engines.item-preview.api                              :as item-preview]
              [random.api                                     :as random]
              [vector.api                                     :as vector]
              [re-frame.api                                          :as r]

              ; TEMP
              [plugins.dnd-kit.api :as dnd-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)}
  ; @param (namespaced map) template-link
  ;  {:id (string)}
  [_ {:keys [disabled?] :as preview-props} {:template/keys [id]}]
  (let [template-name             @(r/subscribe [:x.db/get-item [:price-quote-templates :preview/downloaded-items id :name]])
        template-issuer-name      @(r/subscribe [:x.db/get-item [:price-quote-templates :preview/downloaded-items id :issuer-name]])
        template-default-currency @(r/subscribe [:x.db/get-item [:price-quote-templates :preview/downloaded-items id :default-currency]])
        template-language         @(r/subscribe [:x.db/get-item [:price-quote-templates :preview/downloaded-items id :language]])]
       [common/data-table {:disabled? disabled?
                           :rows [[{:content :name}              {:content template-name             :color :muted :placeholder :unnamed-price-quote-template}]
                                  [{:content :issuer-name}       {:content template-issuer-name      :color :muted :placeholder "-"}]
                                  [{:content :default-currency}  {:content template-default-currency :color :muted :placeholder "-"}]
                                  [{:content :language}          {:content template-language         :color :muted :placeholder "-"}]]}]))

(defn- template-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {}
  ; @param (namespaced map) template-link
  ;  {:template/id (string)}
  [_ {:keys [disabled?]} {:template/keys [id]}]
  (let [issuer-logo @(r/subscribe [:x.db/get-item [:price-quote-templates :preview/downloaded-items id :issuer-logo]])]
       ; XXX#0059 (app.clients.frontend.preview.views)
       [storage/media-preview {:disabled?   disabled?
                               :items       [issuer-logo]
                               :placeholder :empty-thumbnail
                               :thumbnail   {:height :xl
                                             :width  :3xl}}]))

(defn- template-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) template-link
  [preview-id preview-props template-link]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "12px" :align-items "flex-start"}}
        [template-preview-thumbnail preview-id preview-props template-link]
        [template-preview-data      preview-id preview-props template-link]])

(defn- template-preview-static-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) template-link
  ;  {}
  [preview-id preview-props {:template/keys [id] :as template-link}]
  ; BUG#9980 (app.products.frontend.preview.views)
  ; BUG#8871 (app.products.frontend.preview.views)
  (if id [item-preview/body (keyword id)
                            {:error-element   [common/error-element {:error :the-content-has-been-broken}]
                             :ghost-element   #'common/item-preview-ghost-element
                             :preview-element [template-preview-element preview-id preview-props template-link]
                             :item-id         id
                             :item-path       [:price-quote-templates :preview/downloaded-items id]
                             :transfer-id     :price-quote-templates.preview}]))

(defn- template-preview-sortable-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (integer) item-dex
  ; @param (namespaced map) template-link
  ;  {:template/id (string)}
  ; @param (map) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)
  [preview-id preview-props item-dex {:template/keys [id] :as template-link} {:keys [handle-attributes item-attributes]}]
  [:div (update item-attributes :style merge {:align-items "center" :display "flex" :grid-column-gap "18px"})
        (if @(r/subscribe [:item-preview/data-received? (keyword id)])
             [common/list-item-drag-handle {:drag-attributes handle-attributes}])
        [template-preview-static-body preview-id preview-props template-link]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-preview-static-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list template-link] (conj preview-list [template-preview-static-body preview-id preview-props template-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- template-preview-sortable-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)
  ;   :value-path (vector)}
  [preview-id {:keys [items value-path] :as preview-props}]
  [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}
        [dnd-kit/body preview-id
                      {:common-props     preview-props
                       :items            items
                       :item-id-f        :template/id
                       :item-element     #'template-preview-sortable-body
                       :on-order-changed (fn [_ _ %] (r/dispatch-sync [:x.db/set-item! value-path %]))}]])

(defn- template-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:sortable? (boolean)(opt)}
  [preview-id {:keys [sortable?] :as preview-props}]
  (if sortable? [template-preview-sortable-list preview-id preview-props]
                [template-preview-static-list   preview-id preview-props]))

(defn- template-preview-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? info-text label]}]
  (if label [elements/label {:content     label
                             :disabled?   disabled?
                             :line-height :block
                             :info-text   info-text}]))

(defn- template-preview-placeholder
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

(defn- template-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:indent (map)(opt)
  ;   :items (namespaced maps in vector)(opt)}
  [preview-id {:keys [indent items] :as preview-props}]
  [elements/blank preview-id
                  {:content [:<> [template-preview-label preview-id preview-props]
                                 (if (vector/nonempty? items)
                                     [template-preview-list        preview-id preview-props]
                                     [template-preview-placeholder preview-id preview-props])]
                   :indent  indent}])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :items (namespaced maps in vector)(opt)
  ;    [{:template/id (string)}
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
  ;  [template-preview {...}]
  ;
  ; @usage
  ;  [template-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [template-preview preview-id preview-props])))
