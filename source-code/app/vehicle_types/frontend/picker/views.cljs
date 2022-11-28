
(ns app.vehicle-types.frontend.picker.views
    (:require [app.common.frontend.api                      :as common]
              [app.components.frontend.api                  :as components]
              [app.vehicle-types.frontend.picker.prototypes :as picker.prototypes]
              [app.vehicle-types.frontend.preview.views     :as preview.views]
              [elements.api                                 :as elements]
              [random.api                                   :as random]
              [re-frame.api                                 :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-list-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:countable? (boolean)(opt)
  ;   :sortable? (boolean)(opt)}
  ; @param (integer) item-dex
  ; @param (namespaced map) type-link
  ;  {:type/count (integer)(opt)
  ;   :type/id (string)}
  ; @param (map)(opt) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)}
  ([picker-id picker-props item-dex type-link]
   [type-list-item picker-id picker-props item-dex type-link {}])

  ([picker-id {:keys [countable? sortable?]} item-dex {:type/keys [count id]} {:keys [handle-attributes item-attributes]}]
   (let [{:keys [item-number modified-at name quantity-unit]} @(r/subscribe [:item-lister/get-item picker-id id])
         timestamp   @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
         picked-count {:content :n-pieces :replacements [count]}]
        [components/item-list-row {:drag-attributes item-attributes
                                   :cells [(if sortable? [components/list-item-drag-handle {:drag-attributes handle-attributes}])
                                           [components/list-item-thumbnail {:icon :text_snippet :icon-family :material-icons-outlined}]
                                           [components/list-item-cell      {:rows [{:content name :placeholder :unnamed-vehicle-type}]}]
                                           [components/list-item-gap       {:width 12}]
                                           (if countable? [components/list-item-cell {:rows [{:content picked-count :font-size :xs :color :muted}] :width 100}])
                                           (if countable? [components/list-item-gap  {:width 12}])
                                           [components/list-item-cell      {:rows [{:content timestamp :font-size :xs :color :muted}]  :width 100}]]
                                   :border (if (not= item-dex 0) :top)}])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) type-link
  [picker-id picker-props type-link]
  (let [preview-props (picker.prototypes/preview-props-prototype picker-id picker-props type-link)]
       [preview.views/element picker-id preview-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-list-header
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:countable? (boolean)(opt)
  ;   :sortable? (boolean)(opt)}
  [_ {:keys [countable? sortable?]}]
  [components/item-list-header ::type-list-header
                               {:cells [(if sortable? {:width 24})
                                        {:width 84}
                                        {:label :name}
                                        {:width 12}
                                        (if countable? {:label :quantity :width 100})
                                        (if countable? {:width 12})
                                        {:label :modified :width 100}]
                                :border :bottom}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  [common/item-picker picker-id (assoc picker-props :item-element      #'type-item
                                                    :list-item-element #'type-list-item
                                                    :item-list-header  #'type-list-header)])

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
  ;  [type-picker {...}]
  ;
  ; @usage
  ;  [type-picker :my-type-picker {...}]
  ([picker-props]
   [element (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (let [picker-props (picker.prototypes/picker-props-prototype picker-id picker-props)]
        [type-picker picker-id picker-props])))
