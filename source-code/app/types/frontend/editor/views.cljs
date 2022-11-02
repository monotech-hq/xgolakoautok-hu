
(ns app.types.frontend.editor.views
    (:require [app.common.frontend.api            :as common]
              [app.schemes.frontend.api           :as schemes]
              [app.storage.frontend.api           :as storage]
              [app.types.frontend.handler.queries :as handler.queries]
              [elements.api                       :as elements]
              [engines.item-editor.api            :as item-editor]
              [forms.api                          :as forms]
              [layouts.surface-a.api              :as surface-a]
              [mid-fruits.vector                  :as vector]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-scheme-field
  [field-dex field-id]
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])]
       [schemes/scheme-field-block :types.technical-data field-id {:autofocus?   (= 0 field-dex)
                                                                   :disabled?    editor-disabled?
                                                                   :options-path [:types :editor/suggestions field-id]
                                                                   :value-path   [:types :editor/edited-item field-id]}]))

(defn- type-scheme-form
  []
  (letfn [(f [field-list field-dex field-id] (conj field-list [type-scheme-field field-dex field-id]))]
         (let [scheme-field-ids @(r/subscribe [:schemes.form-handler/get-scheme-field-ids :types.technical-data])]
              (reduce-kv f [:<>] scheme-field-ids))))

(defn- type-technical-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])]
       [common/surface-box ::type-technical-data-box
                           {:content [:<> [schemes/add-field-bar :types.technical-data :types.editor {:disabled? editor-disabled?}]
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
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])]
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
                              :value-path    [:types :editor/edited-item :images]}]))

(defn- type-images-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])]
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

(defn- type-files
  []
  [:<>])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-transport-cost-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])]
       [elements/combo-box ::type-transport-cost-field
                           {:disabled?      editor-disabled?
                            :emptiable?     false
                            :end-adornments [{:label "EUR"}]
                            :indent         {:top :m :vertical :s}
                            :label          :transport-cost
                            :options-path   [:types :editor/suggestions :transport-cost]
                            :value-path     [:types :editor/edited-item :transport-cost]}]))

(defn- type-manufacturer-price-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])]
       [elements/combo-box ::type-manufacturer-price-field
                           {:disabled?      editor-disabled?
                            :emptiable?     false
                            :end-adornments [{:label "EUR"}]
                            :indent         {:top :m :vertical :s}
                            :label          :manufacturer-price
                            :options-path   [:types :editor/suggestions :manufacturer-price]
                            :value-path     [:types :editor/edited-item :manufacturer-price]}]))

(defn- type-price-margin-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])]
       [elements/combo-box ::type-price-margin-field
                           {:disabled?      editor-disabled?
                            :emptiable?     false
                            :end-adornments [{:label "%"}]
                            :indent         {:top :m :vertical :s}
                            :label          :price-margin
                            :options-path   [:types :editor/suggestions :price-margin]
                            :value-path     [:types :editor/edited-item :price-margin]}]))

(defn- type-dealer-rebate-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])]
       [elements/combo-box ::type-dealer-rebate-field
                           {:disabled?      editor-disabled?
                            :emptiable?     false
                            :end-adornments [{:label "%"}]
                            :indent         {:top :m :vertical :s}
                            :label          :dealer-rebate
                            :options-path   [:types :editor/suggestions :dealer-rebate]
                            :value-path     [:types :editor/edited-item :dealer-rebate]}]))

(defn- type-price-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])]
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
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])]
       [elements/combo-box ::type-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :emptiable?   false
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:types :editor/suggestions :name]
                            :placeholder  :type-name-placeholder
                            :required?    true
                            :value-path   [:types :editor/edited-item :name]}]))

(defn- type-basic-data
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])]
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
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :types.editor])]
       (case current-view-id :data           [type-data]
                             :images         [type-images]
                             :files          [type-files]
                             :price          [type-price]
                             :technical-data [type-technical-data])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])
        scheme-field-ids @(r/subscribe [:schemes.form-handler/get-scheme-field-ids :types.technical-data])]
       [common/item-editor-menu-bar :types.editor
                                    {:disabled?  editor-disabled?
                                     :menu-items [{:label :data           :change-keys [:name]}
                                                  {:label :images         :change-keys [:images]}
                                                  {:label :files          :change-keys []}
                                                  {:label :price          :change-keys [:manufacturer-price :price-margin :dealer-rebate]}
                                                  {:label :technical-data :change-keys scheme-field-ids}]}]))

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])]
       [common/item-editor-controls :types.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])
        type-name        @(r/subscribe [:db/get-item [:types :editor/edited-item :name]])
        model-name       @(r/subscribe [:db/get-item [:types :handler/model-item :name]])
        type-id          @(r/subscribe [:router/get-current-route-path-param :item-id])
        model-id         @(r/subscribe [:router/get-current-route-path-param :model-id])
        model-uri         (str "/@app-home/models/" model-id "/types")
        type-uri          (str "/@app-home/models/" model-id "/types/" type-id)]
       [common/surface-breadcrumbs :types.editor/view
                                   {:crumbs (if type-id [{:label :app-home  :route "/@app-home"}
                                                         {:label :models    :route "/@app-home/models"}
                                                         {:label model-name :route model-uri :placeholder :unnamed-model}
                                                         {:label type-name  :route type-uri  :placeholder :unnamed-type}
                                                         {:label :edit!}]
                                                        [{:label :app-home  :route "/@app-home"}
                                                         {:label :models    :route "/@app-home/models"}
                                                         {:label model-name :route model-uri :placeholder :unnamed-model}
                                                         {:label :add-type!}])
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :types.editor])
        type-name        @(r/subscribe [:db/get-item [:types :editor/edited-item :name]])]
       [common/surface-label :types.editor/view
                             {:disabled?   editor-disabled?
                              :label       type-name
                              :placeholder :unnamed-type}]))

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
  (let [model-id         @(r/subscribe [:router/get-current-route-path-param :model-id])
        scheme-field-ids @(r/subscribe [:schemes.form-handler/get-scheme-field-ids :types.technical-data])]
       [item-editor/body :types.editor
                         {:auto-title?      true
                          :form-element     #'view-structure
                          :error-element    [common/error-content {:error :the-item-you-opened-may-be-broken}]
                          :ghost-element    #'common/item-editor-ghost-element
                          :initial-item     {:model-id model-id}
                          :item-path        [:types :editor/edited-item]
                          :label-key        :name
                          :query            (vector/concat-items (schemes/request-scheme-form-query :types.technical-data)
                                                                 (handler.queries/request-model-name-query))
                          :suggestion-keys  (vector/concat-items scheme-field-ids
                                                                 [:name :manufacturer-price :price-margin :dealer-rebate])
                          :suggestions-path [:types :editor/suggestions]}]))

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'type-editor}])
