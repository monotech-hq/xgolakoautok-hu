
(ns app.packages.frontend.picker.views
    (:require [app.common.frontend.api                 :as common]
              [app.components.frontend.api             :as components]
              [app.packages.frontend.picker.prototypes :as picker.prototypes]
              [app.packages.frontend.preview.views     :as preview.views]
              [elements.api                            :as elements]
              [random.api                              :as random]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-list-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:countable? (boolean)(opt)
  ;   :sortable? (boolean)(opt)}
  ; @param (integer) item-dex
  ; @param (namespaced map) package-link
  ;  {:package/count (integer)(opt)
  ;   :package/id (string)}
  ; @param (map)(opt) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)}
  ([picker-id picker-props item-dex package-link]
   [package-list-item picker-id picker-props item-dex package-link {}])

  ([picker-id {:keys [countable? sortable?]} item-dex {:package/keys [count id]} {:keys [handle-attributes item-attributes]}]
   (let [{:keys [item-number modified-at name quantity-unit thumbnail]} @(r/subscribe [:item-lister/get-item picker-id id])
         timestamp   @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
         picked-count {:content (:value quantity-unit) :replacements [count]}]
        [components/item-list-row {:drag-attributes item-attributes
                                   :cells [(if sortable? [components/list-item-drag-handle {:drag-attributes handle-attributes}])
                                           (if sortable? [components/list-item-gap         {:width 12}])
                                           [components/list-item-thumbnail {:thumbnail (:media/uri thumbnail)}]
                                           [components/list-item-gap       {:width 12}]
                                           [components/list-item-cell      {:rows [{:content name :placeholder :unnamed-package}]}]
                                           [components/list-item-gap       {:width 12}]
                                           (if countable? [components/list-item-cell {:rows [{:content picked-count :font-size :xs :color :muted}] :width 100}])
                                           (if countable? [components/list-item-gap  {:width 12}])
                                           [components/list-item-cell      {:rows [{:content item-number :font-size :xs :color :muted}] :width 100}]
                                           [components/list-item-gap       {:width 12}]
                                           [components/list-item-cell      {:rows [{:content timestamp :font-size :xs :color :muted}] :width 100}]]
                                   :border (if (not= item-dex 0) :top)}])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) package-link
  [picker-id picker-props package-link]
  (let [preview-props (picker.prototypes/preview-props-prototype picker-id picker-props package-link)]
       [preview.views/element picker-id preview-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-list-header
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:countable? (boolean)(opt)
  ;   :sortable? (boolean)(opt)}
  [_ {:keys [countable? sortable?]}]
  [components/item-list-header ::package-list-header
                               {:cells [(if sortable? {:width 24})
                                        (if sortable? {:width 12})
                                        {:width 84}
                                        {:width 12}
                                        {:label :product-description-short}
                                        {:width 12}
                                        (if countable? {:label :quantity :width 100})
                                        (if countable? {:width 12})
                                        {:label :item-number :width 100}
                                        {:width 12}
                                        {:label :modified :width 100}]
                                :border :bottom}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  [common/item-picker picker-id (assoc picker-props :item-element      #'package-item
                                                    :list-item-element #'package-list-item
                                                    :item-list-header  #'package-list-header)])

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
  ;  [package-picker {...}]
  ;
  ; @usage
  ;  [package-picker :my-package-picker {...}]
  ([picker-props]
   [element (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (let [picker-props (picker.prototypes/picker-props-prototype picker-id picker-props)]
        [package-picker picker-id picker-props])))
