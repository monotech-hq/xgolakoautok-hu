
(ns app.price-quote-templates.frontend.viewer.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.contents.frontend.api   :as contents]
              [app.storage.frontend.api    :as storage]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-default-currency
  []
  (let [viewer-disabled?          @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-default-currency @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :default-currency]])]
       [components/data-element ::template-default-currency
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :default-currency
                                 :placeholder "n/a"
                                 :value       template-default-currency}]))

(defn- template-language
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-language @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :language]])]
       [components/data-element ::template-language
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :language
                                 :placeholder "n/a"
                                 :value       template-language}]))

(defn- template-settings-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [components/surface-box ::template-settings-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [template-language]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [template-default-currency]]
                                                    [:div (forms/form-block-attributes {:ratio 34})]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :settings}]))

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
                                  :item-link   template-informations
                                  :label       :informational-content
                                  :placeholder "n/a"}]))

(defn- template-informations-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [components/surface-box ::template-informational-content-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-informations-preview]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :informations}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-footer-content
  []
  (let [viewer-disabled?        @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-footer-content @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :footer-content]])]
       [components/data-element ::template-footer-content
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :footer-content
                                 :placeholder "n/a"
                                 :value       template-footer-content}]))

(defn- template-footer-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [components/surface-box ::template-footer-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-footer-content]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :footer}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-body-title
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-body-title @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :body-title]])]
       [components/data-element ::template-body-title
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :body-title
                                 :placeholder "n/a"
                                 :value       template-body-title}]))

(defn- template-body-subtitle
  []
  (let [viewer-disabled?       @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-body-subtitle @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :body-subtitle]])]
       [components/data-element ::template-body-subtitle
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :body-subtitle
                                 :placeholder "n/a"
                                 :value       template-body-subtitle}]))

(defn- template-body-description
  []
  (let [viewer-disabled?          @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-body-description @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :body-description]])]
       [components/data-element ::template-body-description
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :body-description
                                 :placeholder "n/a"
                                 :value       template-body-description}]))

(defn- template-body-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [components/surface-box ::template-body-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-body-title]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-body-subtitle]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-body-description]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :body}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-issuer-details
  []
  (let [viewer-disabled?        @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-issuer-details @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :issuer-details]])]
       [components/data-element ::template-issuer-details
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :issuer-description
                                 :placeholder "n/a"
                                 :value       template-issuer-details}]))

(defn- template-issuer-name
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-issuer-name @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :issuer-name]])]
       [components/data-element ::template-issuer-name
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :issuer-name
                                 :placeholder "n/a"
                                 :value       template-issuer-name}]))

(defn- template-issuer-details-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [components/surface-box ::template-issuer-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-issuer-name]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [template-issuer-details]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :issuer-details}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-issuer-logo-picker
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [storage/media-picker ::template-issuer-logo-picker
                             {:autosave?     true
                              :disabled?     viewer-disabled?
                              :indent        {:top :m :vertical :s}
                              :multi-select? false
                              :placeholder   "n/a"
                              :value-path    [:price-quote-templates :viewer/viewed-item :issuer-logo]

                              ; TEMP#0051 (source-code/app/common/item_picker/views.cljs)
                              :read-only? true}]))

(defn- template-issuer-logo-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [components/surface-box ::template-issuer-logo-box
                               {:content [:<> [template-issuer-logo-picker]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :issuer-logo}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])
        template-name    @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :name]])]
       [components/data-element ::template-name
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :name
                                 :placeholder "n/a"
                                 :value       template-name}]))

(defn- template-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quote-templates.viewer])]
       [components/surface-box ::template-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [template-name]]
                                                    [:div (forms/form-block-attributes {:ratio 33})]
                                                    [:div (forms/form-block-attributes {:ratio 34})]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :data}]))
