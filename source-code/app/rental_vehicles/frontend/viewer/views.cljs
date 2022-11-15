
(ns app.rental-vehicles.frontend.viewer.views
    (:require [app.common.frontend.api   :as common]
              [app.contents.frontend.api :as contents]
              [app.storage.frontend.api  :as storage]
              [elements.api              :as elements]
              [engines.item-lister.api   :as item-lister]
              [engines.item-viewer.api   :as item-viewer]
              [forms.api                 :as forms]
              [layouts.surface-a.api     :as surface-a]
              [re-frame.api              :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [data-received? @(r/subscribe [:item-viewer/data-received? :rental-vehicles.viewer])]
          [common/item-viewer-item-info :rental-vehicles.viewer {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-description-preview
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-description @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :description]])]
       [contents/content-preview ::vehicle-description
                                 {:color       :muted
                                  :disabled?   viewer-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :items       [vehicle-description]
                                  :placeholder "-"}]))

(defn- vehicle-description-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [common/surface-box ::vehicle-description-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [vehicle-description-preview]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :description}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-content
  []
  [:<> [vehicle-description-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-images-preview
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-images   @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :images]])]
       [storage/media-preview ::vehicle-image-list
                              {:disabled?   viewer-disabled?
                               :items       vehicle-images
                               :indent      {:top :m :vertical :s}
                               :placeholder "-"}]))

(defn- vehicle-images-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [common/surface-box ::vehicle-images-box
                           {:content [:<> [vehicle-images-preview]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :images}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-images
  []
  [:<> [vehicle-images-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-thumbnail-preview
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-thumbnail @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :thumbnail]])]
       [storage/media-preview ::vehicle-thumbnail
                              {:disabled?   viewer-disabled?
                               :indent      {:top :m :vertical :s}
                               :items       [vehicle-thumbnail]
                               :placeholder "-"}]))

(defn- vehicle-thumbnail-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [common/surface-box ::vehicle-thumbnail-box
                           {:content [:<> [vehicle-thumbnail-preview]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-thumbnail
  []
  [:<> [vehicle-thumbnail-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-number-of-seats
  []
  (let [viewer-disabled?        @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-number-of-seats @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :number-of-seats]])]
       [common/data-element ::vehicle-number-of-seats
                            {:indent      {:top :m :vertical :s}
                             :label       :number-of-seats
                             :placeholder "-"
                             :value       vehicle-number-of-seats}]))

(defn- vehicle-number-of-bunks
  []
  (let [viewer-disabled?        @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-number-of-bunks @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :number-of-bunks]])]
       [common/data-element ::vehicle-number-of-bunks
                            {:indent      {:top :m :vertical :s}
                             :label       :number-of-bunks
                             :placeholder "-"
                             :value       vehicle-number-of-bunks}]))

(defn- vehicle-construction-year
  []
  (let [viewer-disabled?          @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-construction-year @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :construction-year]])]
       [common/data-element ::vehicle-construction-year
                            {:indent      {:top :m :vertical :s}
                             :label       :construction-year
                             :placeholder "-"
                             :value       vehicle-construction-year}]))

(defn- vehicle-technical-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [common/surface-box ::vehicle-technical-data-box
                           {:indent  {:top :m}
                            :content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [vehicle-construction-year]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [vehicle-number-of-seats]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      [vehicle-number-of-bunks]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :technical-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-visibility
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-visibility  @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :visibility]])]
       [common/data-element ::vehicle-visibility
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :visibility-on-the-website
                             :value     (case vehicle-visibility :public :public-content :private :private-content)}]))

(defn- vehicle-public-link
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-public-link @(r/subscribe [:rental-vehicles.viewer/get-vehicle-public-link])]
       [common/data-element ::vehicle-public-link
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :public-link
                             :value     vehicle-public-link}]))

(defn- vehicle-more-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [common/surface-box ::vehicle-more-data-box
                           {:indent  {:top :m}
                            :content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [vehicle-visibility]]
                                                [:div (forms/form-block-attributes {:ratio 67})
                                                      [vehicle-public-link]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :more-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-name     @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :name]])]
       [common/data-element ::vehicle-name
                            {:indent      {:top :m :vertical :s}
                             :label       :name
                             :placeholder "-"
                             :value       vehicle-name}]))

(defn- vehicle-type
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-type     @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :type]])]
       [common/data-element ::vehicle-type
                            {:indent      {:top :m :vertical :s}
                             :label       :type
                             :placeholder "-"
                             :value       vehicle-type}]))

(defn- vehicle-basic-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])]
       [common/surface-box ::vehicle-basic-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 66})
                                                      [vehicle-name]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      [vehicle-type]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :basic-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-overview
  []
  [:<> [vehicle-basic-data-box]
       [vehicle-technical-data-box]
       [vehicle-more-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  [common/item-viewer-menu-bar :rental-vehicles.viewer
                               {:menu-items [{:label :overview}
                                             {:label :thumbnail}
                                             {:label :images}
                                             {:label :content}]}])

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :rental-vehicles.viewer])]
       (case current-view-id :overview  [vehicle-overview]
                             :thumbnail [vehicle-thumbnail]
                             :images    [vehicle-images]
                             :content   [vehicle-content])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-id       @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        edit-item-uri    (str "/@app-home/rental-vehicles/"vehicle-id"/edit")]
       [common/item-viewer-controls :rental-vehicles.viewer
                                    {:disabled?     viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-name     @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :name]])]
       [common/surface-breadcrumbs :rental-vehicles.viewer/view
                                   {:crumbs [{:label :app-home    :route "/@app-home"}
                                             {:label :rental-vehicles    :route "/@app-home/rental-vehicles"}
                                             {:label vehicle-name :placeholder :unnamed-vehicle}]
                                    :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :rental-vehicles.viewer])
        vehicle-name     @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :name]])]
       [common/surface-label :rental-vehicles.viewer/view
                             {:disabled?   viewer-disabled?
                              :label       vehicle-name
                              :placeholder :unnamed-vehicle}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
             [:div [label]
                   [breadcrumbs]]
             [:div [controls]]]
       [elements/horizontal-separator {:size :xxl}]
       [menu-bar]])

(defn- view-structure
  []
  [:<> [header]
       [body]
       [footer]])

(defn- vehicle-viewer
  []
  [item-viewer/body :rental-vehicles.viewer
                    {:auto-title?   true
                     :error-element [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:rental-vehicles :viewer/viewed-item]
                     :label-key     :name}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'vehicle-viewer}])
