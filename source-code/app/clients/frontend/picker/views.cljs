
(ns app.clients.frontend.picker.views
    (:require [app.common.frontend.api                :as common]
              [app.components.frontend.api            :as components]
              [app.clients.frontend.picker.prototypes :as picker.prototypes]
              [app.clients.frontend.preview.views     :as preview.views]
              [elements.api                           :as elements]
              [random.api                             :as random]
              [re-frame.api                           :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-list-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:sortable? (boolean)(opt)}
  ; @param (integer) item-dex
  ; @param (namespaced map) client-link
  ;  {:client/id (string)}
  ; @param (map)(opt) drag-props
  ;  {:handle-attributes (map)
  ;   :item-attributes (map)}
  ([picker-id picker-props item-dex client-link]
   [client-list-item picker-id picker-props item-dex client-link {}])

  ([picker-id {:keys [sortable?]} item-dex {:client/keys [id]} {:keys [handle-attributes item-attributes]}]
   (let [{:keys [colors email-address first-name last-name modified-at phone-number]} @(r/subscribe [:item-lister/get-item picker-id id])
         timestamp   @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
         client-name @(r/subscribe [:clients.picker/get-client-name item-dex])]
        [components/item-list-row {:drag-attributes item-attributes
                                   :cells [(if sortable? [components/list-item-drag-handle {:drag-attributes handle-attributes}])
                                           [components/list-item-avatar {:colors colors :first-name first-name :last-name last-name :size 42}]
                                           [components/list-item-cell   {:rows [{:content client-name :placeholder :unnamed-client}]}]
                                           [components/list-item-gap    {:width 12}]
                                           [components/list-item-cell   {:rows [{:content email-address :font-size :xs :color :muted}] :width 160}]
                                           [components/list-item-gap    {:width 12}]
                                           [components/list-item-cell   {:rows [{:content phone-number :font-size :xs :color :muted}] :width 160}]
                                           [components/list-item-gap    {:width 12}]
                                           [components/list-item-cell   {:rows [{:content timestamp :font-size :xs :color :muted}] :width 100}]]
                                   :border (if (not= item-dex 0) :top)}])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-item
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (namespaced map) client-link
  [picker-id picker-props client-link]
  (let [preview-props (picker.prototypes/preview-props-prototype picker-id picker-props client-link)]
       [preview.views/element picker-id preview-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-list-header
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:sortable? (boolean)(opt)}
  [_ {:keys [sortable?]}]
  [components/item-list-header ::client-list-header
                               {:cells [(if sortable? {:width 24})
                                        {:width 78}
                                        {:label :name}
                                        {:width 12}
                                        {:label :email-address :width 160}
                                        {:width 12}
                                        {:label :phone-number :width 160}
                                        {:width 12}
                                        {:label :modified :width 100}]
                                :border :bottom}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  [common/item-picker picker-id (assoc picker-props :item-element      #'client-item
                                                    :list-item-element #'client-list-item
                                                    :item-list-header  #'client-list-header)])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:autosave? (boolean)(opt)
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
  ;  [client-picker {...}]
  ;
  ; @usage
  ;  [client-picker :my-client-picker {...}]
  ([picker-props]
   [element (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (let [picker-props (picker.prototypes/picker-props-prototype picker-id picker-props)]
        [client-picker picker-id picker-props])))
