
(ns app.products.frontend.preview.views
    (:require [app.common.frontend.api                  :as common]
              [app.components.frontend.api              :as components]
              [app.products.frontend.preview.prototypes :as preview.prototypes]
              [elements.api                             :as elements]
              [random.api                               :as random]
              [re-frame.api                             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;    {:product/count (integer)(opt)
  ;     :product/id (string)}}
  [_ {{:product/keys [count id]} :item-link :keys [disabled?]}]
  (let [product-name                @(r/subscribe [:x.db/get-item [:products :preview/downloaded-items id :name]])
        product-item-number         @(r/subscribe [:x.db/get-item [:products :preview/downloaded-items id :item-number]])
        product-unit-price          @(r/subscribe [:x.db/get-item [:products :preview/downloaded-items id :unit-price] 0])
        product-quantity-unit-value @(r/subscribe [:x.db/get-item [:products :preview/downloaded-items id :quantity-unit :value]])
        product-quantity             {:content product-quantity-unit-value :replacements [count]}]
       [components/data-table {:disabled? disabled?
                               :rows [[          {:content :name}        {:content product-name                    :color :muted :selectable? true :placeholder :unnamed-product}]
                                      [          {:content :item-number} {:content product-item-number             :color :muted :selectable? true :placeholder "n/a"}]
                                      (if count [{:content :quantity}    {:content product-quantity                :color :muted :selectable? true :placeholder "n/a"}])
                                      [          {:content :unit-price}  {:content (str product-unit-price " EUR") :color :muted :selectable? true :placeholder "n/a"}]]}]))

(defn- product-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;    {:product/id (string)}}
  [_ {{:product/keys [id]} :item-link :keys [disabled?]}]
  ; XXX#0059 (source-code/app/clients/frontend/preview/views.cljs)
  (let [thumbnail @(r/subscribe [:x.db/get-item [:products :preview/downloaded-items id :thumbnail]])]
       [elements/thumbnail ::product-preview-thumbnail
                           {:border-radius :s
                            :disabled?     disabled?
                            :height        :3xl
                            :width         :5xl
                            :uri           (:media/uri thumbnail)}]))

(defn- product-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "12px" :align-items "flex-start"}}
        [product-preview-thumbnail preview-id preview-props]
        [product-preview-data      preview-id preview-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [common/item-preview preview-id (assoc preview-props :preview-element #'product-preview-element)])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :item-link (namespaced map)(opt)
  ;    {:product/count (integer)
  ;     :product/id (string)}
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [product-preview {...}]
  ;
  ; @usage
  ;  [product-preview :my-product-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [product-preview preview-id preview-props])))
