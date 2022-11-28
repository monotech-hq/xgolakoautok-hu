
(ns app.vehicle-models.frontend.preview.views
    (:require [app.common.frontend.api                        :as common]
              [app.components.frontend.api                    :as components]
              [app.vehicle-models.frontend.preview.prototypes :as preview.prototypes]
              [elements.api                                   :as elements]
              [random.api                                     :as random]
              [re-frame.api                                   :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-preview-data
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;   :item-link (namespaced map)
  ;    {:model/count (integer)
  ;     :model/id (string)}}
  [_ {{:model/keys [count id]} :item-link :keys [disabled?]}]
  (let [model-name                @(r/subscribe [:x.db/get-item [:vehicle-models :preview/downloaded-items id :name]])
        model-product-description @(r/subscribe [:x.db/get-item [:vehicle-models :preview/downloaded-items id :product-description] 0])
        model-quantity             {:content :n-pieces :replacements [count]}]
       [components/data-table {:disabled? disabled?
                               :rows [[          {:content :name}                {:content model-name                :color :muted :selectable? true :placeholder :unnamed-vehicle-model}]
                                      [          {:content :product-description} {:content model-product-description :color :muted :selectable? true :placeholder "n/a"}]
                                      (if count [{:content :quantity}            {:content model-quantity            :color :muted :selectable? true :placeholder "n/a"}])]}]))

(defn- model-preview-thumbnail
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  ;  {}
  [_ {{:model/keys [id]} :item-link :keys [disabled?]}]
  ; XXX#0059 (source-code/app/clients/frontend/preview/views.cljs)
  (let [thumbnail @(r/subscribe [:x.db/get-item [:vehicle-models :preview/downloaded-items id :thumbnail]])]
       [elements/thumbnail ::model-preview-thumbnail
                           {:border-radius :s
                            :disabled?     disabled?
                            :height        :3xl
                            :width         :5xl
                            :uri           (:media/uri thumbnail)}]))

(defn- model-preview-element
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [:div {:style {:display "flex" :flex-wrap "wrap" :grid-gap "12px" :align-items "flex-start"}}
        [model-preview-thumbnail preview-id preview-props]
        [model-preview-data      preview-id preview-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-preview
  ; @param (keyword) preview-id
  ; @param (map) preview-props
  [preview-id preview-props]
  [common/item-preview preview-id (assoc preview-props :preview-element #'model-preview-element)])

(defn element
  ; @param (keyword)(opt) preview-id
  ; @param (map) preview-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :item-link (namespaced maps in vector)(opt)
  ;    {:model/count (integer)
  ;     :model/id (string)}
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [model-preview {...}]
  ;
  ; @usage
  ;  [model-preview :my-model-preview {...}]
  ([preview-props]
   [element (random/generate-keyword) preview-props])

  ([preview-id preview-props]
   (let [preview-props (preview.prototypes/preview-props-prototype preview-id preview-props)]
        [model-preview preview-id preview-props])))
