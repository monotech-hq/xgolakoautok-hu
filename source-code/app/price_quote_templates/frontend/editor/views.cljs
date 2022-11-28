
(ns app.price-quote-templates.frontend.editor.views
  (:require [app.common.frontend.api                         :as common]
            [app.components.frontend.api                     :as components]
            [app.price-quote-templates.frontend.editor.boxes :as editor.boxes]
            [app.price-quote-templates.mid.handler.config    :as handler.config]
            [elements.api                                    :as elements]
            [layouts.surface-a.api                           :as surface-a]
            [re-frame.api                                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-editor-footer :price-quote-templates.editor {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-footer
  []
  [:<> [editor.boxes/template-footer-box]])

(defn- template-informations
  []
  [:<> [editor.boxes/template-informational-content-box]])

(defn- template-header
  []
  [:<> [editor.boxes/template-issuer-logo-box]
       [editor.boxes/template-issuer-details-box]])

(defn- template-body
  []
  [:<> [editor.boxes/template-body-box]])

(defn- template-settings
  []
  [:<> [editor.boxes/template-settings-box]])

(defn- template-data
  []
  [:<> [editor.boxes/template-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :price-quote-templates.editor])]
       (case current-view-id :data         [template-data]
                             :header       [template-header]
                             :body         [template-body]
                             :footer       [template-footer]
                             :informations [template-informations]
                             :settings     [template-settings])))

(defn- body
  []
  ; XXX#5050 (source-code/app/price_quote_templates/frontend/README.md)
  [common/item-editor-body :price-quote-templates.editor
                           {:default-item  {:default-currency handler.config/DEFAULT-DEFAULT-CURRENCY
                                            :language         handler.config/DEFAULT-LANGUAGE}
                            :item-path     [:price-quote-templates :editor/edited-item]
                            :form-element  [view-selector]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [template-name  @(r/subscribe [:x.db/get-item [:price-quote-templates :editor/edited-item :name]])
        template-id    @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        template-route @(r/subscribe [:item-editor/get-item-route :price-quote-templates.editor template-id])]
       [common/item-editor-header :price-quote-templates.editor
                                  {:label       template-name
                                   :placeholder :unnamed-price-quote-template
                                   :crumbs      [{:label :app-home              :route "/@app-home"}
                                                 {:label :price-quote-templates :route "/@app-home/price-quote-templates"}
                                                 {:label template-name          :route template-route :placeholder :unnamed-price-quote-template}]
                                   :menu-items  [{:label :data         :change-keys [:name]}
                                                 {:label :header       :change-keys [:issuer-logo :issuer-name :issuer-details]}
                                                 {:label :body         :change-keys [:body-title :body-subtitle]}
                                                 {:label :footer       :change-keys [:footer-content]}
                                                 {:label :informations :change-keys [:informations]}
                                                 {:label :settings     :change-keys [:default-currency :language]}]}]))
                                                ;{:label :preview}

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
