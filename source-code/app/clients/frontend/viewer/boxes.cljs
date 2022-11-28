
(ns app.clients.frontend.viewer.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]
              [x.locales.api               :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-last-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-last-name @(r/subscribe [:x.db/get-item [:clients :viewer/viewed-item :last-name]])]
       [components/data-element ::client-last-name
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :last-name
                                 :placeholder "n/a"
                                 :value       client-last-name}]))

(defn- client-first-name
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-first-name @(r/subscribe [:x.db/get-item [:clients :viewer/viewed-item :first-name]])]
       [components/data-element ::client-first-name
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :first-name
                                 :placeholder "n/a"
                                 :value       client-first-name}]))

(defn- client-name
  []
  [:div (forms/form-row-attributes)
        [x.locales/name-order [:div (forms/form-block-attributes {:ratio 50})
                                    [client-first-name]]
                              [:div (forms/form-block-attributes {:ratio 50})
                                    [client-last-name]]
                              @(r/subscribe [:x.locales/get-name-order])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-email-address
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-email-address @(r/subscribe [:x.db/get-item [:clients :viewer/viewed-item :email-address]])]
       [components/data-element ::client-email-address
                                {:copyable?   true
                                 :disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :email-address
                                 :marked?     true
                                 :placeholder "n/a"
                                 :value       client-email-address}]))

(defn- client-phone-number
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-phone-number @(r/subscribe [:x.db/get-item [:clients :viewer/viewed-item :phone-number]])]
       [components/data-element ::client-phone-number
                                {:copyable?   true
                                 :disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :phone-number
                                 :marked?     true
                                 :placeholder "n/a"
                                 :value       client-phone-number}]))

(defn- client-primary-contacts
  []
  [:div (forms/form-row-attributes)
        [:div (forms/form-block-attributes {:ratio 50})
              [client-email-address]]
        [:div (forms/form-block-attributes {:ratio 50})
              [client-phone-number]]])

(defn- client-basic-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])]
       [components/surface-box ::client-basic-data-box
                               {:content [:<> [client-name]
                                              [client-primary-contacts]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :basic-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-company-name
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-company-name @(r/subscribe [:x.db/get-item [:clients :viewer/viewed-item :company-name]])]
       [components/data-element ::client-company-name
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :company-name
                                 :placeholder "n/a"
                                 :value       client-company-name}]))

(defn- client-company-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])]
       [components/surface-box ::client-company-data-box
                               {:indent  {:top :m}
                                :content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [client-company-name]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :company-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-vat-no
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-vat-no    @(r/subscribe [:x.db/get-item [:clients :viewer/viewed-item :vat-no]])]
       [components/data-element ::client-vat-no
                                {:copyable?   true
                                 :disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :vat-no
                                 :marked?     true
                                 :placeholder "n/a"
                                 :value       client-vat-no}]))

(defn- client-country
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-country   @(r/subscribe [:x.db/get-item [:clients :viewer/viewed-item :country]])]
       [components/data-element ::client-country
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :country
                                 :placeholder "n/a"
                                 :value       client-country}]))

(defn- client-zip-code
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-zip-code  @(r/subscribe [:x.db/get-item [:clients :viewer/viewed-item :zip-code]])]
       [components/data-element ::client-zip-code
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :zip-code
                                 :placeholder "n/a"
                                 :value       client-zip-code}]))

(defn- client-city
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-city      @(r/subscribe [:x.db/get-item [:clients :viewer/viewed-item :city]])]
       [components/data-element ::client-city
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :city
                                 :placeholder "n/a"
                                 :value       client-city}]))

(defn- client-address
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-address   @(r/subscribe [:x.db/get-item [:clients :viewer/viewed-item :address]])]
       [components/data-element ::client-address
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :address
                                 :placeholder "n/a"
                                 :value       client-address}]))

(defn- client-billing-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])]
       [components/surface-box ::client-billing-data-box
                               {:indent  {:top :m}
                                :content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [client-country]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [client-zip-code]]
                                                    [:div (forms/form-block-attributes {:ratio 34})
                                                          [client-city]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [client-address]]
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [client-vat-no]]
                                                    [:div (forms/form-block-attributes {:ratio 34})]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :billing-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-tags
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])]
       [elements/chip-group {:disabled?           viewer-disabled?
                             :horizontal-position :left
                             :indent              {:top :m :vertical :s}
                             :label               :tags
                             :placeholder         "n/a"
                             :value-path          [:clients :viewer/viewed-item :tags]}]))

(defn- client-colors
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-colors    @(r/subscribe [:x.db/get-item [:clients :viewer/viewed-item :colors]])]
       [elements/color-stamp {:colors    client-colors
                              :disabled? viewer-disabled?
                              :indent    {:top :m :vertical :s}
                              :label     :color
                              :size      :xxl}]))

(defn- client-description
  []
  (let [viewer-disabled?   @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-description @(r/subscribe [:x.db/get-item [:clients :viewer/viewed-item :description]])]
       [components/data-element ::client-description
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :description
                                 :placeholder "n/a"
                                 :value       client-description}]))

(defn- client-more-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])]
       [components/surface-box ::client-more-data-box
                               {:indent  {:top :m}
                                :content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 33})
                                                          [client-colors]]
                                                    [:div (forms/form-block-attributes {:ratio 67})
                                                          [client-tags]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [client-description]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :more-data}]))
