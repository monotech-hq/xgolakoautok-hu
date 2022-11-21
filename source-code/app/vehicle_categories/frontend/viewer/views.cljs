
(ns app.vehicle-categories.frontend.viewer.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [engines.item-viewer.api     :as item-viewer]
              [engines.item-lister.api     :as item-lister]
              [forms.api                   :as forms]
              [layouts.surface-a.api       :as surface-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [data-received? @(r/subscribe [:item-viewer/data-received? :vehicle-categories.viewer])]
          [common/item-viewer-item-info :vehicle-categories.viewer {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-item-structure
  [lister-id item-dex {:keys [modified-at name thumbnail]}]
  (let [timestamp  @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last? @(r/subscribe [:item-lister/item-last? lister-id item-dex])]
       [common/list-item-structure {:cells [[ {:thumbnail (:media/uri thumbnail)}]
                                            [components/list-item-cell   {:rows [{:content name :placeholder :vehicle-unnamed-model}
                                                                                 {:content timestamp :font-size :xs :color :muted}]
                                                                          :width :stretch}]
                                            [components/list-item-marker {:icon :drag_handle :style {:cursor :grab}}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- model-item
  [lister-id item-dex {:keys [id] :as model-item}]
  (let [category-id @(r/subscribe [:x.router/get-current-route-path-param :item-id])]
       [elements/toggle {:content     [model-item-structure lister-id item-dex model-item]
                         :hover-color :highlight
                         :on-click    [:x.router/go-to! (str "/@app-home/models/"id)
                                                      {:route-parent (str "/@app-home/vehicle-categories/"category-id"/models")}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-lister-action-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        on-click [:models.selector/load-selector! :vehicle-models.selector
                                                  {:autosave?     false
                                                   :multi-select? true
                                                   :on-change     [:vehicle-categories.viewer/save-category-models!]
                                                   :value-path    [:vehicle-categories :viewer/viewed-item :models]}]]
       [common/action-bar ::model-lister-action-bar
                          {:disabled? viewer-disabled?
                           :indent    {:bottom :xs}
                           :label     :select-vehicle-models!
                           :on-click  on-click}]))

(defn- model-list
  [lister-id]
  (let [items @(r/subscribe [:item-lister/get-downloaded-items lister-id])]
       [common/item-list lister-id {:item-element #'model-item :items items}]))

(defn- model-lister
  []
  (let [category-models @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :models]])]
       [item-lister/body :vehicle-models.lister
                         {:default-order-by :modified-at/descending
                          :items-path    [:vehicle-models :lister/downloaded-items]
                          :error-element [components/error-content {:error :the-content-you-opened-may-be-broken}]
                          :ghost-element #'common/item-lister-ghost-element
                          :placeholder   :no-items-to-show
                          :list-element  #'model-list
                          :prefilter     {:$or category-models}}]))

(defn- category-vehicle-models-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])]
       [common/surface-box ::category-vehicle-models-box
                           {:content [:<> [model-lister-action-bar]
                                          [model-lister]]
                            :disabled? viewer-disabled?
                            :label     :vehicle-models
                            :overflow  :hidden}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-vehicle-models
  []
  [:<> [category-vehicle-models-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-thumbnail-preview
  []
  (let [viewer-disabled?   @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-thumbnail @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :thumbnail]])]
       [storage/media-preview ::category-thumbnail-preview
                              {:disabled?   viewer-disabled?
                               :indent      {:top :m :vertical :s}
                               :items       [category-thumbnail]
                               :placeholder "-"
                               :thumbnail   {:height :3xl :width :5xl}}]))

(defn- category-thumbnail-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])]
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
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-visibility @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :visibility]])]
       [common/data-element ::category-visibility
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :visibility-on-the-website
                             :value     (case category-visibility :public :public-content :private :private-content)}]))

(defn- category-public-link
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-public-link @(r/subscribe [:vehicle-categories.viewer/get-category-public-link])]
       [common/data-element ::category-public-link
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :public-link
                             :value     category-public-link}]))

(defn- category-description
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-description @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :description]])]
       [common/data-element ::category-description
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :description
                             :placeholder "-"
                             :value       category-description}]))

(defn- category-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-name    @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :name]])]
       [common/data-element ::category-name
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :name
                             :placeholder "-"
                             :value       category-name}]))

(defn- category-model-count
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-models  @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :models]])
        category-model-count (count category-models)]
       [common/data-element ::category-model-count
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :vehicle-model-count
                             :value     (str category-model-count)}]))

(defn- category-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])]
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
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :vehicle-categories.viewer])]
       (case current-view-id :overview       [category-overview]
                             :vehicle-models [category-vehicle-models])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])]
       [common/item-viewer-menu-bar :vehicle-categories.viewer
                                    {:menu-items [{:label :overview}
                                                  {:label :vehicle-models}]
                                     :disabled? viewer-disabled?}]))

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-id      @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        edit-item-uri     (str "/@app-home/vehicle-categories/"category-id"/edit")]
       [common/item-viewer-controls :vehicle-categories.viewer
                                    {:disabled?      viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-name    @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :name]])]
       [components/surface-breadcrumbs ::breadcrumbs
                                       {:crumbs [{:label :app-home           :route "/@app-home"}
                                                 {:label :vehicle-categories :route "/@app-home/vehicle-categories"}
                                                 {:label category-name :placeholder :unnamed-vehicle-category}]
                                        :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-categories.viewer])
        category-name    @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :name]])]
       [components/surface-label ::label
                                 {:disabled?   viewer-disabled?
                                  :label       category-name
                                  :placeholder :unnamed-vehicle-category}]))

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
       [body]
       [footer]])

(defn- category-viewer
  ; @param (keyword) surface-id
  [_]
  [item-viewer/body :vehicle-categories.viewer
                    {:auto-title?   true
                     :error-element [components/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:vehicle-categories :viewer/viewed-item]
                     :label-key     :name}])

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'category-viewer}])
