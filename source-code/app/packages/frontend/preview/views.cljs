
(ns app.packages.frontend.preview.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-preview.api :as item-preview]
              [mid-fruits.random        :as random]
              [mid-fruits.vector        :as vector]
              [re-frame.api             :as r]))

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
  (let [package-name                @(r/subscribe [:db/get-item [:packages :preview/downloaded-items id :name]])
        package-item-number         @(r/subscribe [:db/get-item [:packages :preview/downloaded-items id :item-number]])
        package-price               @(r/subscribe [:db/get-item [:packages :preview/downloaded-items id :price] 0])
        package-quantity-unit-value @(r/subscribe [:db/get-item [:packages :preview/downloaded-items id :quantity-unit :value]])
        package-quantity             {:content package-quantity-unit-value :replacements [count]}]
       [common/data-table {:disabled? disabled?
                           :rows [[          {:content :name}        {:content package-name               :color :muted :placeholder :unnamed-package}]
                                  [          {:content :item-number} {:content package-item-number        :color :muted :placeholder "-"}]
                                  (if count [{:content :quantity}    {:content package-quantity           :color :muted :placeholder "-"}])
                                  [          {:content :unit-price}  {:content (str package-price " EUR") :color :muted :placeholder "-"}]]}]))

(defn- package-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {}
  ; @param (namespaced map) package-link
  ;  {:package/id (string)}
  [_ {:keys [disabled?]} {:package/keys [id]}]
  (let [thumbnail @(r/subscribe [:db/get-item [:packages :preview/downloaded-items id :thumbnail]])]
       [storage/media-preview {:disabled?   disabled?
                               :items       [thumbnail]
                               :placeholder :empty-thumbnail
                               :thumbnail   {:height :xl
                                             :width  :3xl}}]))

(defn- package-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) package-link
  [preview-id preview-props package-link]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-column-gap "12px" :align-items "flex-start"}}
        [package-preview-thumbnail preview-id preview-props package-link]
        [package-preview-data      preview-id preview-props package-link]])

(defn- package-preview-body
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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list package-link] (conj preview-list [package-preview-body preview-id preview-props package-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- package-preview-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? info-text label]}]
  (if label [elements/label {:content   label
                             :disabled? disabled?
                             :info-text info-text}]))

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
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [package-preview {...}]
  ;
  ; @usage
  ;  [package-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   [package-preview preview-id preview-props]))
