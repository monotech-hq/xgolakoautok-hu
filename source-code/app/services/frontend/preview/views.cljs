
(ns app.services.frontend.preview.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-preview.api :as item-preview]
              [mid-fruits.random        :as random]
              [mid-fruits.vector        :as vector]
              [re-frame.api             :as r]))

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
  (let [service-name                @(r/subscribe [:db/get-item [:services :preview/downloaded-items id :name]])
        service-item-number         @(r/subscribe [:db/get-item [:services :preview/downloaded-items id :item-number]])
        service-price               @(r/subscribe [:db/get-item [:services :preview/downloaded-items id :price] 0])
        service-quantity-unit-value @(r/subscribe [:db/get-item [:services :preview/downloaded-items id :quantity-unit :value]])
        service-quantity             {:content service-quantity-unit-value :replacements [count]}]
       [common/data-table {:disabled? disabled?
                           :rows [[          {:content :name}        {:content service-name               :color :muted :placeholder :unnamed-service}]
                                  [          {:content :item-number} {:content service-item-number        :color :muted :placeholder "-"}]
                                  (if count [{:content :quantity}    {:content service-quantity           :color :muted :placeholder "-"}])
                                  [          {:content :unit-price}  {:content (str service-price " EUR") :color :muted :placeholder "-"}]]}]))

(defn- service-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {}
  ; @param (namespaced map) service-link
  ;  {:service/id (string)}
  [_ {:keys [disabled?]} {:service/keys [id]}]
  (let [thumbnail @(r/subscribe [:db/get-item [:services :preview/downloaded-items id :thumbnail]])]
       [storage/media-preview {:disabled?   disabled?
                               :items       [thumbnail]
                               :placeholder :empty-thumbnail
                               :thumbnail   {:height :xl
                                             :width  :3xl}}]))

(defn- service-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) service-link
  ;  {}
  [preview-id preview-props]
  [service-preview-data preview-id preview-props])

(defn- service-preview-body
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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list service-link] (conj preview-list [service-preview-body preview-id preview-props service-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- service-preview-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? info-text label]}]
  (if label [elements/label {:content   label
                             :disabled? disabled?
                             :info-text info-text}]))

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
                                   :selectable? true}]))

(defn- service-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:indent (map)(opt)
  ;   :items (namespaced maps in vector)(opt)}
  [preview-id {:keys [indent items] :as preview-props}]
  [elements/blank preview-id
                  {:content [:<> [service-preview-label preview-id preview-props]
                                 (if (vector/nonempty? items)
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
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [service-preview {...}]
  ;
  ; @usage
  ;  [service-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   [service-preview preview-id preview-props]))
