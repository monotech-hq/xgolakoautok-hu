
(ns app.clients.frontend.preview.views
    (:require [app.common.frontend.api  :as common]
              [elements.api             :as elements]
              [engines.item-preview.api :as item-preview]
              [mid-fruits.random        :as random]
              [mid-fruits.vector        :as vector]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)}
  ; @param (namespaced map) client-link
  ;  {:id (string)}
  [_ {:keys [disabled?] :as preview-props} {:client/keys [id]}]
  (let [client-name          @(r/subscribe [:clients.preview/get-client-name id])
        client-email-address @(r/subscribe [:db/get-item [:clients :preview/downloaded-items id :phone-number]])
        client-phone-number  @(r/subscribe [:db/get-item [:clients :preview/downloaded-items id :phone-number]])
        client-address       @(r/subscribe [:clients.preview/get-client-address              id])
        client-company-name  @(r/subscribe [:db/get-item [:clients :preview/downloaded-items id :company-name]])
        client-vat-no        @(r/subscribe [:db/get-item [:clients :preview/downloaded-items id :vat-no]])]
       [common/data-table {:disabled? disabled?
                           :rows [[{:content :name}
                                   {:content client-name          :color :muted :placeholder :unnamed-client}]
                                  [{:content :email-address}
                                   {:content client-email-address :color :muted :placeholder "-"}]
                                  [{:content :phone-number}
                                   {:content client-phone-number  :color :muted :placeholder "-"}]
                                  [{:content :address}
                                   {:content client-address       :color :muted :placeholder "-"}]
                                  [{:content :company-name}
                                   {:content client-company-name  :color :muted :placeholder "-"}]
                                  [{:content :vat-no}
                                   {:content client-vat-no        :color :muted :placeholder "-"}]]}]))

(defn- client-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) client-link
  [preview-id preview-props client-link]
  [client-preview-data preview-id preview-props client-link])

(defn- client-preview-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) client-link
  ;  {}
  [preview-id preview-props {:client/keys [id] :as client-link}]
  ; BUG#9980 (app.products.frontend.preview.views)
  ; BUG#8871 (app.products.frontend.preview.views)
  (if id [item-preview/body (keyword id)
                            {:error-element   [common/error-element {:error :the-content-has-been-broken}]
                             :ghost-element   #'common/item-preview-ghost-element
                             :preview-element [client-preview-element preview-id preview-props client-link]
                             :item-id         id
                             :item-path       [:clients :preview/downloaded-items id]
                             :transfer-id     :clients.preview}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list client-link] (conj preview-list [client-preview-body preview-id preview-props client-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- client-preview-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? info-text label]}]
  (if label [elements/label {:content   label
                             :disabled? disabled?
                             :info-text info-text}]))

(defn- client-preview-placeholder
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

(defn- client-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:indent (map)(opt)
  ;   :items (namespaced maps in vector)(opt)}
  [preview-id {:keys [indent items] :as preview-props}]
  [elements/blank preview-id
                  {:content [:<> [client-preview-label preview-id preview-props]
                                 (if (vector/nonempty? items)
                                     [client-preview-list        preview-id preview-props]
                                     [client-preview-placeholder preview-id preview-props])]
                   :indent  indent}])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :items (namespaced maps in vector)(opt)
  ;    [{:client/id (string)}
  ;     {...}]
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [client-preview {...}]
  ;
  ; @usage
  ;  [client-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   [client-preview preview-id preview-props]))
