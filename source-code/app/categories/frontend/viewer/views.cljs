
(ns app.categories.frontend.viewer.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-viewer.api  :as item-viewer]
              [engines.item-lister.api  :as item-lister]
              [forms.api                :as forms]
              [layouts.surface-a.api    :as surface-a]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-item-structure
  [lister-id item-dex {:keys [modified-at name thumbnail]}]
  (let [timestamp @(r/subscribe [:activities/get-actual-timestamp modified-at])]
       [common/list-item-structure lister-id item-dex
                                   {:cells [[common/list-item-thumbnail    lister-id item-dex {:thumbnail (:media/uri thumbnail)}]
                                            [common/list-item-primary-cell lister-id item-dex {:label name :description timestamp :stretch? true
                                                                                               :placeholder :unnamed-model}]
                                            [common/list-item-marker       lister-id item-dex {:icon :drag_handle :style {:cursor :grab}}]]}]))

(defn- model-item
  [lister-id item-dex {:keys [id] :as model-item}]
  (let [category-id @(r/subscribe [:router/get-current-route-path-param :item-id])]
       [elements/toggle {:content     [model-item-structure lister-id item-dex model-item]
                         :hover-color :highlight
                         :on-click    [:router/go-to! (str "/@app-home/models/"id)
                                                      {:route-parent (str "/@app-home/categories/"category-id"/models")}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-lister-action-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :categories.viewer])
        on-click [:models.selector/load-selector! :models.selector
                                                  {:autosave?     false
                                                   :multi-select? true
                                                   :on-change     [:categories.viewer/save-category-models!]
                                                   :value-path    [:categories :viewer/viewed-item :models]}]]
       [common/action-bar ::model-lister-action-bar
                          {:disabled? viewer-disabled?
                           :indent    {:bottom :xs}
                           :label     :select-models!
                           :on-click  on-click}]))

(defn- model-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'model-item :items items}])

(defn- model-lister
  []
  (let [category-models @(r/subscribe [:db/get-item [:categories :viewer/viewed-item :models]])]
       [item-lister/body :models.lister
                         {:default-order-by :modified-at/descending
                          :items-path    [:models :lister/downloaded-items]
                          :error-element [common/error-content {:error :the-content-you-opened-may-be-broken}]
                          :ghost-element #'common/item-lister-ghost-element
                          :placeholder   :no-models-selected
                          :list-element  #'model-list
                          :prefilter     {:$or category-models}}]))

(defn- category-models-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-editor/editor-disabled? :categories.viewer])]
       [common/surface-box ::category-models-box
                           {:content [:<> [model-lister-action-bar]
                                          [model-lister]]
                            :disabled? viewer-disabled?
                            :label     :models
                            :overflow  :hidden}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-models
  []
  [:<> [category-models-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-thumbnail-preview
  []
  (let [viewer-disabled?   @(r/subscribe [:item-viewer/viewer-disabled? :categories.viewer])
        category-thumbnail @(r/subscribe [:db/get-item [:categories :viewer/viewed-item :thumbnail]])]
       [storage/media-preview ::category-thumbnail-preview
                              {:disabled?   viewer-disabled?
                               :indent      {:top :m :vertical :s}
                               :items       [category-thumbnail]
                               :placeholder "-"}]))

(defn- category-thumbnail-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :categories.viewer])]
       [common/surface-box ::category-thumbnail-box
                           {:indent  {:top :m}
                            :content [:<> [category-thumbnail-preview]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-visibility
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :categories.viewer])
        category-visibility  @(r/subscribe [:db/get-item [:categories :viewer/viewed-item :visibility]])]
       [common/data-element ::category-visibility
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :visibility-on-the-website
                             :value     (case category-visibility :public :public-content :private :private-content)}]))

(defn- category-public-link
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :categories.viewer])
        category-public-link @(r/subscribe [:categories.viewer/get-category-public-link])]
       [common/data-element ::category-public-link
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :public-link
                             :value     category-public-link}]))

(defn- category-description
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :categories.viewer])
        category-description @(r/subscribe [:db/get-item [:categories :viewer/viewed-item :description]])]
       [common/data-element ::category-description
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :description
                             :placeholder "-"
                             :value       category-description}]))

(defn- category-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :categories.viewer])
        category-name    @(r/subscribe [:db/get-item [:categories :viewer/viewed-item :name]])]
       [common/data-element ::category-name
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :name
                             :placeholder "-"
                             :value       category-name}]))

(defn- category-model-count
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :categories.viewer])
        category-models     @(r/subscribe [:db/get-item [:categories :viewer/viewed-item :models]])
        category-model-count  (count category-models)]
       [common/data-element ::category-model-count
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :model-count
                             :value     (str category-model-count)}]))

(defn- category-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :categories.viewer])]
       [common/surface-box ::category-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [category-name]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [category-model-count]]
                                                [:div (forms/form-block-attributes {:ratio 34})]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [category-description]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [category-visibility]]
                                                [:div (forms/form-block-attributes {:ratio 67})
                                                      [category-public-link]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-overview
  []
  [:<> [category-data-box]
       [category-thumbnail-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :categories.viewer])]
       (case current-view-id :overview [category-overview]
                             :models   [category-models])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :categories.viewer])]
       [common/item-viewer-menu-bar :categories.viewer
                                    {:menu-items [{:label :overview}
                                                  {:label :models}]
                                     :disabled? viewer-disabled?}]))

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :categories.viewer])
        category-id      @(r/subscribe [:router/get-current-route-path-param :item-id])
        edit-item-uri     (str "/@app-home/categories/"category-id"/edit")]
       [common/item-viewer-controls :categories.viewer
                                    {:disabled?      viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :categories.viewer])
        category-name    @(r/subscribe [:db/get-item [:categories :viewer/viewed-item :name]])]
       [common/surface-breadcrumbs :categories.viewer/view
                                   {:crumbs [{:label :app-home     :route "/@app-home"}
                                             {:label :categories   :route "/@app-home/categories"}
                                             {:label category-name :placeholder :unnamed-category}]
                                    :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :categories.viewer])
        category-name    @(r/subscribe [:db/get-item [:categories :viewer/viewed-item :name]])]
       [common/surface-label :categories.viewer/view
                             {:disabled?   viewer-disabled?
                              :label       category-name
                              :placeholder :unnamed-category}]))

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

(defn- category-viewer
  []
  [item-viewer/body :categories.viewer
                    {:auto-title?   true
                     :error-element [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:categories :viewer/viewed-item]
                     :label-key     :name}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'category-viewer}])
