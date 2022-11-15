
(ns app.price-quote-templates.frontend.viewer.views
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
  (if-let [data-received? @(r/subscribe [:item-viewer/data-received? :price-quote-templates.viewer])]
          [common/item-viewer-item-info :price-quote-templates.viewer {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-default-currency
  []
  (let [viewer-disabled?          @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-default-currency @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :default-currency]])]
       [common/data-element ::template-default-currency
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :default-currency
                             :placeholder "-"
                             :value       template-default-currency}]))

(defn- template-language
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-language @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :language]])]
       [common/data-element ::template-language
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :language
                             :placeholder "-"
                             :value       template-language}]))

(defn- template-settings-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [common/surface-box ::template-settings-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [template-language]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [template-default-currency]]
                                                [:div (forms/form-block-attributes {:ratio 34})]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :settings}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-settings
  []
  [:<> [template-settings-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-informations-preview
  []
  (let [viewer-disabled?      @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-informations @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :informations]])]
       [contents/content-preview ::template-informations-preview
                                 {:color       :muted
                                  :disabled?   viewer-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :items       [template-informations]
                                  :label       :informational-content
                                  :placeholder "-"}]))

(defn- template-informations-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [common/surface-box ::template-informational-content-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [template-informations-preview]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :informations}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-informations
  []
  [:<> [template-informations-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-footer-content
  []
  (let [viewer-disabled?        @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-footer-content @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :footer-content]])]
       [common/data-element ::template-footer-content
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :footer-content
                             :placeholder "-"
                             :value       template-footer-content}]))

(defn- template-footer-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [common/surface-box ::template-footer-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [template-footer-content]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :footer}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-footer
  []
  [:<> [template-footer-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-body-title
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-body-title @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :body-title]])]
       [common/data-element ::template-body-title
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :body-title
                             :placeholder "-"
                             :value       template-body-title}]))

(defn- template-body-subtitle
  []
  (let [viewer-disabled?       @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-body-subtitle @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :body-subtitle]])]
       [common/data-element ::template-body-subtitle
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :body-subtitle
                             :placeholder "-"
                             :value       template-body-subtitle}]))

(defn- template-body-description
  []
  (let [viewer-disabled?          @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-body-description @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :body-description]])]
       [common/data-element ::template-body-description
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :body-description
                             :placeholder "-"
                             :value       template-body-description}]))

(defn- template-body-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [common/surface-box ::template-body-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [template-body-title]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [template-body-subtitle]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [template-body-description]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :body}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-body
  []
  [:<> [template-body-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-issuer-details
  []
  (let [viewer-disabled?        @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-issuer-details @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :issuer-details]])]
       [common/data-element ::template-issuer-details
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :issuer-description
                             :placeholder "-"
                             :value       template-issuer-details}]))

(defn- template-issuer-name
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-issuer-name @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :issuer-name]])]
       [common/data-element ::template-issuer-name
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :issuer-name
                             :placeholder "-"
                             :value       template-issuer-name}]))

(defn- template-issuer-details-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [common/surface-box ::template-issuer-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [template-issuer-name]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [template-issuer-details]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :issuer-details}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-issuer-logo-preview
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-issuer-logo @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :issuer-logo]])]
       [storage/media-preview ::template-issuer-logo-preview
                              {:disabled?   viewer-disabled?
                               :indent      {:top :m :vertical :s}
                               :items       [template-issuer-logo]
                               :placeholder "-"}]))

(defn- template-issuer-logo-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [common/surface-box ::template-issuer-logo-box
                           {:content [:<> [template-issuer-logo-preview]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :issuer-logo}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-header
  []
  [:<> [template-issuer-logo-box]
       [template-issuer-details-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-name    @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :name]])]
       [common/data-element ::template-name
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :name
                             :placeholder "-"
                             :value       template-name}]))

(defn- template-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [common/surface-box ::template-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [template-name]]
                                                [:div (forms/form-block-attributes {:ratio 33})]
                                                [:div (forms/form-block-attributes {:ratio 34})]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-data
  []
  [:<> [template-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-overview
  []
  [:<> [template-data]
       [template-header]
       [template-body]
       [template-footer]
       [template-informations]
       [template-settings]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :price-quote-templates.viewer])]
       (case current-view-id :overview [template-overview])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [common/item-viewer-menu-bar :price-quote-templates.viewer
                                    {:disabled?  viewer-disabled?
                                     :menu-items [{:label :overview}]}]))

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-id      @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        edit-item-uri     (str "/@app-home/price-quote-templates/"template-id"/edit")]
       [common/item-viewer-controls :price-quote-templates.viewer
                                    {:disabled?     viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-name    @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :name]])]
       [common/surface-breadcrumbs :price-quote-templates.viewer/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :price-quote-templates    :route "/@app-home/price-quote-templates"}
                                             {:label template-name :placeholder :unnamed-price-quote-template}]
                                    :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-name    @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :name]])]
       [common/surface-label :price-quote-templates.viewer/view
                             {:disabled?   viewer-disabled?
                              :label       template-name
                              :placeholder :unnamed-price-quote-template}]))

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

(defn- template-viewer
  []
  [item-viewer/body :price-quote-templates.viewer
                    {:auto-title?   true
                     :error-element [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:price-quote-templates :viewer/viewed-item]
                     :label-key     :name}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'template-viewer}])
