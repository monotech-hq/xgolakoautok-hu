
(ns app.vehicle-types.frontend.preview.views
    (:require [app.common.frontend.api                       :as common]
              [app.components.frontend.api                   :as components]
              [app.storage.frontend.api                      :as storage]
              [app.vehicle-types.frontend.preview.prototypes :as preview.prototypes]
              [elements.api                                  :as elements]
              [random.api                                    :as random]
              [re-frame.api                                  :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;    {:type/count (integer)
  ;     :type/id (string)}}
  [_ {{:type/keys [count id]} :item-link :keys [disabled?]}]
  (let [type-outer-dimensions   @(r/subscribe [:vehicle-types.preview/get-type-outer-dimensions         id])
        type-inner-dimensions   @(r/subscribe [:vehicle-types.preview/get-type-inner-dimensions         id])
        type-name               @(r/subscribe [:x.db/get-item [:vehicle-types :preview/downloaded-items id :name]])
        type-manufacturer-price @(r/subscribe [:x.db/get-item [:vehicle-types :preview/downloaded-items id :manufacturer-price]])
        type-transport-cost     @(r/subscribe [:x.db/get-item [:vehicle-types :preview/downloaded-items id :transport-cost]])
        type-price-margin       @(r/subscribe [:x.db/get-item [:vehicle-types :preview/downloaded-items id :price-margin]])
        type-dealer-rebate      @(r/subscribe [:x.db/get-item [:vehicle-types :preview/downloaded-items id :dealer-rebate]])
        type-quantity            {:content :n-pieces :replacements [count]}]
       [components/data-table {:disabled? disabled?
                               :rows [[          {:content :name}                 {:content type-name                                         :color :muted :selectable? true :placeholder :unnamed-vehicle-type}]
                                      (if count [{:content :quantity}             {:content type-quantity                                     :color :muted :selectable? true :placeholder "n/a"}])
                                      [          {:content :manufacturer-price}   {:content {:content type-manufacturer-price :suffix " EUR"} :color :muted :selectable? true :placeholder "n/a"}]
                                      [          {:content :transport-cost}       {:content {:content type-transport-cost     :suffix " EUR"} :color :muted :selectable? true :placeholder "n/a"}]
                                      [          {:content :price-margin}         {:content {:content type-price-margin       :suffix " %"}   :color :muted :selectable? true :placeholder "n/a"}]
                                      [          {:content :dealer-rebate}        {:content {:content type-dealer-rebate      :suffix " %"}   :color :muted :selectable? true :placeholder "n/a"}]
                                      [          {:content :outer-dimensions-wlh} {:content type-outer-dimensions                             :color :muted :selectable? true :placeholder "n/a"}]
                                      [          {:content :inner-dimensions-wlh} {:content type-inner-dimensions                             :color :muted :selectable? true :placeholder "n/a"}]]}]))

(defn- type-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [type-preview-data preview-id preview-props])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [common/item-preview preview-id (assoc preview-props :preview-element #'type-preview-element)])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :item-link (namespaced maps in vector)(opt)
  ;    {:type/count (integer)
  ;     :type/id (string)}
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [type-preview {...}]
  ;
  ; @usage
  ;  [type-preview :my-type-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [type-preview preview-id preview-props])))
