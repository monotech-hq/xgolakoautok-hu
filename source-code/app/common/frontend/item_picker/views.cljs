
(ns app.common.frontend.item-picker.views
    (:require [app.components.frontend.api                :as components]
              [app.common.frontend.item-picker.prototypes :as item-picker.prototypes]
              [engines.item-lister.api                    :as item-lister]
              [elements.api                               :as elements]
              [random.api                                 :as random]
              [re-frame.api                               :as r]

              ; TEMP#0880 (source-code/app/common/frontend/item_lister/views.cljs)
              [plugins.dnd-kit.api :as dnd-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- downloading-label
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [_ _]
  [elements/label {:color       :muted
                   :content     :downloading...
                   :font-size   :xs
                   :line-height :block}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- static-item-list
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:list-item-element (component or symbol)
  ;   :value-path (vector)}
  [picker-id {:keys [list-item-element value-path] :as picker-props}]
  (let [picked-items @(r/subscribe [:x.db/get-item value-path])]
       (letfn [(f [item-list item-dex item-link]
                  (conj item-list [list-item-element picker-id picker-props item-dex item-link]))]
              (reduce-kv f [:<>] picked-items))))

(defn- sortable-item-list
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:import-id-f (function)
  ;   :list-item-element (component or symbol)
  ;   :value-path (vector)}
  [picker-id {:keys [import-id-f list-item-element value-path] :as picker-props}]
  (let [picked-items @(r/subscribe [:x.db/get-item value-path])]
       [dnd-kit/body picker-id
                     {:items            picked-items
                      :item-id-f        import-id-f
                      :item-element     [list-item-element picker-id picker-props]
                      :on-order-changed (fn [_ _ %3] (r/dispatch-sync [:x.db/set-item! value-path %3]))}]))

(defn- item-list-header
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:item-list-header (component or symbol)(opt)}
  [picker-id {:keys [item-list-header] :as picker-props}]
  (if item-list-header [item-list-header picker-id picker-props]))

(defn- item-list-body
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:sortable? (boolean)(opt)}
  [picker-id {:keys [sortable?] :as picker-props}]
  (if sortable? [sortable-item-list picker-id picker-props]
                [static-item-list   picker-id picker-props]))

(defn- item-list
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  [picker-id picker-props]
  [components/item-list-table picker-id
                              {:body   [item-list-body   picker-id picker-props]
                               :header [item-list-header picker-id picker-props]}])

(defn- item-lister
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:items-path (vector)
  ;   :transfer-id (keyword)}
  [picker-id {:keys [items-path transfer-id] :as picker-props}]
  (let [item-filters @(r/subscribe [:item-picker/get-item-filters picker-id picker-props])]
       [item-lister/body picker-id
                         {:default-order-by  :modified-at/descending
                          :display-progress? false
                          :items-path        items-path
                          :error-element     [components/error-content {:error :the-content-you-opened-may-be-broken}]
                          :ghost-element     [downloading-label picker-id picker-props]
                          :list-element      [item-list picker-id picker-props]
                          :prefilter         {:$or item-filters}
                          :transfer-id       transfer-id}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-preview
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:item-element (component or symbol)
  ;   :value-path (vector)}
  [picker-id {:keys [item-element value-path] :as picker-props}]
  (let [picked-item @(r/subscribe [:x.db/get-item value-path])]
       [item-element picker-id picker-props picked-item]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-placeholder
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)}
  ;   :placeholder (metamorphic-content)(opt)}
  [_ {:keys [disabled? placeholder]}]
  (if placeholder [elements/label {:color       :highlight
                                   :content     placeholder
                                   :disabled?   disabled?
                                   :font-size   :s
                                   :line-height :block
                                   :selectable? true}]))

(defn- item-picker-button
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-select (metamorphic-event)
  ;   :toggle-label (metamorphic-content)}
  [picker-id {:keys [disabled? on-select toggle-label] :as picker-props}]
  ; TEMP#0051
  (if-not (:read-only? picker-props)
          [:div {:style {:display :flex}}
                [elements/button {:color     :muted
                                  :disabled? disabled?
                                  :font-size :xs
                                  :indent    {:bottom :m}
                                  :label     toggle-label
                                  :on-click  on-select}]]))

(defn- item-picker-label
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)}
  [_ {:keys [disabled? info-text label required?]}]
  (if label [elements/label {:content     label
                             :disabled?   disabled?
                             :info-text   info-text
                             :line-height :block
                             :required?   required?}]))

(defn- item-picker-body
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:multi-select? (boolean)
  ;   :value-path (vector)}
  [picker-id {:keys [multi-select? value-path] :as picker-props}]
  (let [picked-items @(r/subscribe [:x.db/get-item value-path])]
       [:<> [item-picker-label    picker-id picker-props]
            [item-picker-button   picker-id picker-props]
            (cond (empty? picked-items)  [item-placeholder picker-id picker-props]
                  (not    multi-select?) [item-preview     picker-id picker-props]
                  :multi-select?         [item-lister      picker-id picker-props])]))

(defn- item-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:indent (map)(opt)}
  [picker-id {:keys [indent] :as picker-props}]
  [elements/blank picker-id
                  {:content [item-picker-body picker-id picker-props]
                   :indent  indent}])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:autosave? (boolean)(opt)
  ;    Default: false
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :export-filter-f (function)(opt)
  ;    Default: (fn [item-id] {:id item-id})
  ;    W/ {:multi-select? true}
  ;   :import-id-f (function)(opt)
  ;    Default: (fn [{:keys [id] :as item-link}] id)
  ;    W/ {:multi-select? true}
  ;   :indent (map)(opt)
  ;   :info-text (metamorphic-content)(opt)
  ;   :item-element (component or symbol)(opt)
  ;    W/ {:multi-select? false}
  ;   :item-list-header (component or symbol)(opt)
  ;    W/ {:multi-select? true}
  ;   :items-path (vector)(opt)
  ;    Az elemeket megjelenítő engines.item-lister.api/body komponens paramétere.
  ;    W/ {:multi-select? true}
  ;   :label (metamorphic-content)(opt)
  ;   :list-item-element (component or symbol)(opt)
  ;    W/ {:multi-select? true}
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;    W/ {:multi-select? true}
  ;   :multi-select? (boolean)(opt)
  ;    Default: false
  ;   :on-change (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elemet.
  ;   :on-save (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elemet.
  ;   :on-select (metamorphic-event)
  ;   :required? (boolean)(opt)
  ;    Default: false
  ;   :placeholder (metamorphic-content)(opt)
  ;   :toggle-label (metamorphic-content)
  ;   :sortable? (boolean)(opt)
  ;    Default: false
  ;   :transfer-id (keyword)(opt)
  ;    Az elemeket megjelenítő engines.item-lister.api/body komponens paramétere.
  ;    W/ {:multi-select? true}
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [item-picker {...}]
  ;
  ; @usage
  ;  [item-picker :my-item-picker {...}]
  ;
  ; @usage
  ;  (defn my-item-element [picker-id picker-props item-link] ...)
  ;  [item-picker :my-item-picker {:item-element  #'my-item-element
  ;                                :multi-select? false}]
  ;
  ; @usage
  ;  (defn my-list-item-element [picker-id picker-props item-dex item-link] ...)
  ;  [item-picker :my-item-picker {:list-item-element #'my-list-item-element
  ;                                :multi-select?     true}]
  ;
  ; @usage
  ;  (defn my-item-list-header  [picker-id picker-props] ...)
  ;  (defn my-list-item-element [picker-id picker-props item-dex item-link drag-props] ...)
  ;  [item-picker :my-item-picker {:item-list-header  #'my-item-list-header
  ;                                :list-item-element #'my-list-item-element
  ;                                :multi-select?     true
  ;                                :sortable?         true}]
  ([picker-props]
   [element (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   ; TEMP#0051
   ; A box-editor koncepcio bevezetésével, minden viewer felületen
   ; picker-ek fognak megjelenni, de addig a viewer felületeken nem lehet
   ; modositani az adatokat szoval ideiglenesen a read-only? tulajdonság
   ; oldja azt, hogy ne jelenjen meg a select-item(s) gomb.
   ; :read-only? (boolean)(opt)
   (let [picker-props (item-picker.prototypes/picker-props-prototype picker-id picker-props)]
        [item-picker picker-id picker-props])))
