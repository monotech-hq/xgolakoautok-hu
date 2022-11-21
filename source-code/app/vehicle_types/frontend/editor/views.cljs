
(ns app.vehicle-types.frontend.editor.views
    (:require [app.common.frontend.api                    :as common]
              [app.components.frontend.api                :as components]
              [app.schemes.frontend.api                   :as schemes]
              [app.storage.frontend.api                   :as storage]
              [app.vehicle-types.frontend.handler.queries :as handler.queries]
              [elements.api                               :as elements]
              [engines.item-editor.api                    :as item-editor]
              [forms.api                                  :as forms]
              [layouts.surface-a.api                      :as surface-a]
              [re-frame.api                               :as r]
              [vector.api                                 :as vector]
              [x.components.api                           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-scheme-form
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [schemes/scheme-form :vehicle-types.technical-data
                            {:disabled?        editor-disabled?
                             :suggestions-path [:vehicle-types :editor/suggestions]
                             :value-path       [:vehicle-types :editor/edited-item]}]))

(defn- type-technical-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [common/surface-box ::type-technical-data-box
                           {:content [:<> [schemes/scheme-controls :vehicle-types.technical-data {:disabled? editor-disabled?}]
                                          [elements/horizontal-separator {:size :m}]
                                          [type-scheme-form]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :technical-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-technical-data
  []
  [:<> [type-technical-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-image-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [storage/media-picker ::type-image-picker
                             {:autosave?     false
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? true
                              :placeholder   "-"
                              :sortable?     true
                              :toggle-label  :select-images!
                              :thumbnail     {:height :3xl :width :5xl}
                              :value-path    [:vehicle-types :editor/edited-item :images]}]))

(defn- type-images-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [common/surface-box ::type-images-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [type-image-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :images}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-images
  []
  [:<> [type-images-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-file-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [storage/media-picker ::type-file-picker
                             {:autosave?     false
                              :disabled?     editor-disabled?
                              :extensions    ["doc" "xls" "pdf"]
                              :indent        {:vertical :s}
                              :multi-select? true
                              :placeholder   "-"
                              :sortable?     true
                              :toggle-label  :select-files!
                              :thumbnail     {:height :3xl :width :5xl}
                              :value-path    [:vehicle-types :editor/edited-item :files]}]))

(defn- type-files-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [common/surface-box ::type-files-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [type-file-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :files}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-files
  []
  [:<> [type-files-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-transport-cost-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [elements/combo-box ::type-transport-cost-field
                           {:disabled?      editor-disabled?
                            :emptiable?     false
                            :end-adornments [{:label "EUR" :color :muted}]
                            :indent         {:top :m :vertical :s}
                            :label          :transport-cost
                            :options-path   [:vehicle-types :editor/suggestions :transport-cost]
                            :value-path     [:vehicle-types :editor/edited-item :transport-cost]}]))

(defn- type-manufacturer-price-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [elements/combo-box ::type-manufacturer-price-field
                           {:disabled?      editor-disabled?
                            :emptiable?     false
                            :end-adornments [{:label "EUR" :color :muted}]
                            :indent         {:top :m :vertical :s}
                            :label          :manufacturer-price
                            :options-path   [:vehicle-types :editor/suggestions :manufacturer-price]
                            :value-path     [:vehicle-types :editor/edited-item :manufacturer-price]}]))

(defn- type-price-margin-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [elements/combo-box ::type-price-margin-field
                           {:disabled?      editor-disabled?
                            :emptiable?     false
                            :end-adornments [{:label "%" :color :muted}]
                            :indent         {:top :m :vertical :s}
                            :label          :price-margin
                            :options-path   [:vehicle-types :editor/suggestions :price-margin]
                            :value-path     [:vehicle-types :editor/edited-item :price-margin]}]))

(defn- type-dealer-rebate-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [elements/combo-box ::type-dealer-rebate-field
                           {:disabled?      editor-disabled?
                            :emptiable?     false
                            :end-adornments [{:label "%" :color :muted}]
                            :indent         {:top :m :vertical :s}
                            :label          :dealer-rebate
                            :options-path   [:vehicle-types :editor/suggestions :dealer-rebate]
                            :value-path     [:vehicle-types :editor/edited-item :dealer-rebate]}]))

(defn- type-price-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [common/surface-box ::type-price-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [type-manufacturer-price-field]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [type-price-margin-field]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      [type-dealer-rebate-field]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [type-transport-cost-field]]
                                                [:div (forms/form-block-attributes {:ratio 33})]
                                                [:div (forms/form-block-attributes {:ratio 34})]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :price}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-price
  []
  [:<> [type-price-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [elements/combo-box ::type-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:vehicle-types :editor/suggestions :name]
                            :placeholder  :vehicle-type-name-placeholder
                            :required?    true
                            :value-path   [:vehicle-types :editor/edited-item :name]}]))

(defn- type-basic-data
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [common/surface-box ::type-basic-data
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [type-name-field]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :basic-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-data
  []
  [:<> [type-basic-data]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :vehicle-types.editor])]
       (case current-view-id :data           [type-data]
                             :images         [type-images]
                             :files          [type-files]
                             :price          [type-price]
                             :technical-data [type-technical-data])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])
        scheme-field-ids @(r/subscribe [:schemes.form-handler/get-scheme-field-ids :vehicle-types.technical-data])]
       [common/item-editor-menu-bar :vehicle-types.editor
                                    {:disabled?  editor-disabled?
                                     :menu-items [{:label :data           :change-keys [:name]}
                                                  {:label :images         :change-keys [:images]}
                                                  {:label :files          :change-keys [:files]}
                                                  {:label :price          :change-keys [:manufacturer-price :price-margin :dealer-rebate]}
                                                  {:label :technical-data :change-keys scheme-field-ids}]}]))

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])]
       [common/item-editor-controls :vehicle-types.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])
        type-name        @(r/subscribe [:x.db/get-item [:vehicle-types :editor/edited-item :name]])
        model-name       @(r/subscribe [:x.db/get-item [:vehicle-types :handler/model-item :name]])
        type-id          @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        model-id         @(r/subscribe [:x.router/get-current-route-path-param :model-id])
        model-uri         (str "/@app-home/vehicle-models/" model-id "/types")
        type-uri          (str "/@app-home/vehicle-models/" model-id "/types/" type-id)]
       [components/surface-breadcrumbs ::breadcrumbs
                                       {:crumbs (if type-id [{:label :app-home       :route "/@app-home"}
                                                             {:label :vehicle-models :route "/@app-home/vehicle-models"}
                                                             {:label model-name      :route model-uri :placeholder :unnamed-vehicle-model}
                                                             {:label type-name       :route type-uri  :placeholder :unnamed-vehicle-type}
                                                             {:label :edit!}]
                                                            [{:label :app-home       :route "/@app-home"}
                                                             {:label :vehicle-models :route "/@app-home/vehicle-models"}
                                                             {:label model-name      :route model-uri :placeholder :unnamed-vehicle-model}
                                                             {:label :add!}])
                                        :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :vehicle-types.editor])
        type-name        @(r/subscribe [:x.db/get-item [:vehicle-types :editor/edited-item :name]])]
       [components/surface-label ::label
                                 {:disabled?   editor-disabled?
                                  :label       type-name
                                  :placeholder :unnamed-vehicle-type}]))

(defn- header
  []
  [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
             [:div [label]
                   [breadcrumbs]]
             [:div [controls]]]
       [elements/horizontal-separator {:size :xxl}]
       [menu-bar]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  []
  [:<> [header]
       [body]])

(defn- type-editor
  []
  (let [model-id         @(r/subscribe [:x.router/get-current-route-path-param :model-id])
        scheme-field-ids @(r/subscribe [:schemes.form-handler/get-scheme-field-ids :vehicle-types.technical-data])]
       [item-editor/body :vehicle-types.editor
                         {:auto-title?      true
                          :form-element     #'view-structure
                          :error-element    [components/error-content {:error :the-item-you-opened-may-be-broken}]
                          :ghost-element    #'common/item-editor-ghost-element
                          :initial-item     {:model-id model-id}
                          :item-path        [:vehicle-types :editor/edited-item]
                          :label-key        :name
                          :query            (handler.queries/request-model-name-query)
                          :suggestion-keys  (vector/concat-items scheme-field-ids
                                                                 [:name :manufacturer-price :price-margin :dealer-rebate])
                          :suggestions-path [:vehicle-types :editor/suggestions]}]))

(defn- preloader
  [_]
  ; A type-editor komponensben megjelenített item-editor/body komponens scheme-field-ids
  ; értékéhez szükséges előre letölteni a request-scheme-form-query lekérést!
  [x.components/querier :vehicle-types.editor/preloader
                        {:content     #'type-editor
                         :placeholder #'common/item-editor-ghost-element
                         :query       (schemes/request-scheme-form-query :vehicle-types.technical-data)}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'preloader}])
