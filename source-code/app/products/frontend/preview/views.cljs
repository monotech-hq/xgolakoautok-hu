
(ns app.products.frontend.preview.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-preview.api :as item-preview]
              [mid-fruits.random        :as random]
              [mid-fruits.vector        :as vector]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)}
  ; @param (namespaced map) product-link
  ;  {:count (integer)
  ;   :id (string)}
  [_ {:keys [disabled?] :as preview-props} {:product/keys [count id]}]
  (let [product-name                @(r/subscribe [:db/get-item [:products :preview/downloaded-items id :name]])
        product-item-number         @(r/subscribe [:db/get-item [:products :preview/downloaded-items id :item-number]])
        product-price               @(r/subscribe [:db/get-item [:products :preview/downloaded-items id :price] 0])
        product-quantity-unit-value @(r/subscribe [:db/get-item [:products :preview/downloaded-items id :quantity-unit :value]])
        product-quantity             {:content product-quantity-unit-value :replacements [count]}]
       [common/data-table {:disabled? disabled?
                           :rows [[          {:content :name}        {:content product-name               :color :muted :placeholder :unnamed-product}]
                                  [          {:content :item-number} {:content product-item-number        :color :muted :placeholder "-"}]
                                  (if count [{:content :quantity}    {:content product-quantity           :color :muted :placeholder "-"}])
                                  [          {:content :unit-price}  {:content (str product-price " EUR") :color :muted :placeholder "-"}]]}]))

(defn- product-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {}
  ; @param (namespaced map) product-link
  ;  {:product/id (string)}
  [_ {:keys [disabled?]} {:product/keys [id]}]
  (let [thumbnail @(r/subscribe [:db/get-item [:products :preview/downloaded-items id :thumbnail]])]
       [storage/media-preview {:disabled?   disabled?
                               :items       [thumbnail]
                               :placeholder :empty-thumbnail
                               :thumbnail   {:height :xl
                                             :width  :3xl}}]))

(defn- product-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) product-link
  [preview-id preview-props product-link]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-column-gap "12px" :align-items "flex-start"}}
        [product-preview-thumbnail preview-id preview-props product-link]
        [product-preview-data      preview-id preview-props product-link]])

(defn- product-preview-body
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ; @param (namespaced map) product-link
  ;  {}
  [preview-id preview-props {:product/keys [id] :as product-link}]
  ; BUG#9980
  ; Az item-preview/body komponenseket különböző azonosítókkal szükséges meghívni,
  ; hogy külön tudják kezelni az egyes elemekhez letöltött adatokat!
  ;
  ; Generált azonosítót nem lehetséges használni, mert a preview-props térkép
  ; megváltozásakor (pl. ha az item-count értéke megváltozik), az item-preview
  ; plugin példánya elveszítené a kapcsolatot a letöltött adatokkal, a paraméterek
  ; megváltozása miatt újragenerálódó azonosítója miatt!
  ;
  ; BUG#8871
  ; Az item-preview/body komponens számára az első paramétert (preview-id) kötelezően
  ; kulcsszó típusként szükséges átadni!
  ; A product-preview komponens items tulajdonságaként átadott vektor esetlegesen
  ; nil értéket vagy hibás product-link hivatkozást is tartalmazhat, ezért szükséges
  ; vizsgálni az (if id ...) feltétellel az azonosító meglétét ...
  (if id [item-preview/body (keyword id)
                            {:error-element   [common/error-element {:error :the-content-has-been-broken}]
                             :ghost-element   #'common/item-preview-ghost-element
                             :preview-element [product-preview-element preview-id preview-props product-link]
                             :item-id         id
                             :item-path       [:products :preview/downloaded-items id]
                             :transfer-id     :products.preview}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-preview-list
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:items (namespaced maps in vector)}
  [preview-id {:keys [items] :as preview-props}]
  (letfn [(f [preview-list product-link] (conj preview-list [product-preview-body preview-id preview-props product-link]))]
         (reduce f [:div {:style {:display "flex" :flex-direction "column" :grid-row-gap "24px"}}] items)))

(defn- product-preview-label
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? info-text label]}]
  (if label [elements/label {:content   label
                             :disabled? disabled?
                             :info-text info-text}]))

(defn- product-preview-placeholder
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

(defn- product-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:indent (map)(opt)
  ;   :items (namespaced maps in vector)(opt)}
  [preview-id {:keys [indent items] :as preview-props}]
  [elements/blank preview-id
                  {:content [:<> [product-preview-label preview-id preview-props]
                                 (if (vector/nonempty? items)
                                     [product-preview-list        preview-id preview-props]
                                     [product-preview-placeholder preview-id preview-props])]
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
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [product-preview {...}]
  ;
  ; @usage
  ;  [product-preview :my-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   [product-preview preview-id preview-props]))
