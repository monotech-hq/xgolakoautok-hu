
(ns app.vehicle-models.frontend.picker.views
    (:require [app.common.frontend.api                       :as common]
              [app.components.frontend.api                   :as components]
              [app.vehicle-models.frontend.picker.prototypes :as picker.prototypes]
              [app.vehicle-models.frontend.preview.views     :as preview.views]
              [elements.api                                  :as elements]
              [random.api                                    :as random]
              [re-frame.api                                  :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-list-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:countable? (boolean)(opt)
  ;   :sortable? (boolean)(opt)}
  ; @param (integer) item-dex
  ; @param (namespaced map) model-link
  ;  {:model/count (integer)(opt)
  ;   :model/id (string)}
  ; @param (map)(opt) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)}
  ([picker-id picker-props item-dex model-link]
   [model-list-item picker-id picker-props item-dex model-link {}])

  ([picker-id {:keys [countable? sortable?]} item-dex {:model/keys [count id]} {:keys [handle-attributes item-attributes]}]
   (let [{:keys [item-number modified-at name quantity-unit thumbnail]} @(r/subscribe [:item-lister/get-item picker-id id])
         timestamp   @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
         picked-count {:content :n-pieces :replacements [count]}]
        [components/item-list-row {:drag-attributes item-attributes
                                   :cells [(if sortable? [components/list-item-drag-handle {:drag-attributes handle-attributes}])
                                           (if sortable? [components/list-item-gap         {:width 12}])
                                           [components/list-item-thumbnail {:thumbnail (:media/uri thumbnail)}]
                                           [components/list-item-gap       {:width 12}]
                                           [components/list-item-cell      {:rows [{:content name :placeholder :unnamed-vehicle-model}]}]
                                           [components/list-item-gap       {:width 12}]
                                           (if countable? [components/list-item-cell {:rows [{:content picked-count :font-size :xs :color :muted}] :width 100}])
                                           (if countable? [components/list-item-gap  {:width 12}])
                                           [components/list-item-cell      {:rows [{:content timestamp :font-size :xs :color :muted}]  :width 100}]]
                                   :border (if (not= item-dex 0) :top)}])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) model-link
  [picker-id picker-props model-link]
  (let [preview-props (picker.prototypes/preview-props-prototype picker-id picker-props model-link)]
       [preview.views/element picker-id preview-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-list-header
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:countable? (boolean)(opt)
  ;   :sortable? (boolean)(opt)}
  [_ {:keys [countable? sortable?]}]
  [components/item-list-header ::model-list-header
                               {:cells [(if sortable? {:width 24})
                                        (if sortable? {:width 12})
                                        {:width 84}
                                        {:width 12}
                                        {:label :name}
                                        {:width 12}
                                        (if countable? {:label :quantity :width 100})
                                        (if countable? {:width 12})
                                        {:label :modified :width 100}]
                                :border :bottom}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  [common/item-picker picker-id (assoc picker-props :item-element      #'model-item
                                                    :list-item-element #'model-list-item
                                                    :item-list-header  #'model-list-header)])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:autosave? (boolean)(opt)
  ;    Default: false
  ;   :countable? (boolean)(opt)
  ;    Default: false
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;    W/ {:multi-select? true}
  ;   :multi-select? (boolean)(opt)
  ;    Default: false
  ;   :on-change (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elemet.
  ;   :on-save (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elemet.
  ;   :placeholder (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)
  ;    Default: false
  ;   :sortable? (boolean)(opt)
  ;    Default: false
  ;    W/ {:multi-select? true}
  ;   :toggle-label (metamorphic-content)(opt)
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [model-picker {...}]
  ;
  ; @usage
  ;  [model-picker :my-model-picker {...}]
  ([picker-props]
   [element (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (let [picker-props (picker.prototypes/picker-props-prototype picker-id picker-props)]
        [model-picker picker-id picker-props])))
