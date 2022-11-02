
(ns app.price-quote-templates.frontend.preview.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-preview.api :as item-preview]
              [mid-fruits.random        :as random]
              [mid-fruits.vector        :as vector]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)}
  ; @param (namespaced map) template-link
  ;  {:id (string)}
  [_ {:keys [disabled?] :as preview-props} {:template/keys [id]}]
  (let [template-name             @(r/subscribe [:db/get-item [:price-quote-templates :preview/downloaded-items id :name]])
        template-issuer-name      @(r/subscribe [:db/get-item [:price-quote-templates :preview/downloaded-items id :issuer-name]])
        template-default-currency @(r/subscribe [:db/get-item [:price-quote-templates :preview/downloaded-items id :default-currency]])
        template-language         @(r/subscribe [:db/get-item [:price-quote-templates :preview/downloaded-items id :language]])]
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
  (let [issuer-logo @(r/subscribe [:db/get-item [:price-quote-templates :preview/downloaded-items id :issuer-logo]])]
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
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-column-gap "12px" :align-items "flex-start"}}
        [template-preview-thumbnail preview-id preview-props template-link]
        [template-preview-data      preview-id preview-props template-link]])

(defn- template-preview-body
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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list template-link] (conj preview-list [template-preview-body preview-id preview-props template-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- template-preview-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? info-text label]}]
  (if label [elements/label {:content   label
                             :disabled? disabled?
                             :info-text info-text}]))

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
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [template-preview {...}]
  ;
  ; @usage
  ;  [template-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   [template-preview preview-id preview-props]))
