
(ns app.website-content.frontend.editor.views
    (:require [app.common.frontend.api   :as common]
              [app.contents.frontend.api :as contents]
              [app.storage.frontend.api  :as storage]
              [elements.api              :as elements]
              [engines.file-editor.api   :as file-editor]
              [forms.api                 :as forms]
              [layouts.surface-a.api     :as surface-a]
              [mid-fruits.css            :as css]
              [mid-fruits.vector         :as vector]
              [re-frame.api              :as r]))
  
;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- about-us-section-picker
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [contents/content-picker ::about-us-section-picker
                                {:autosave?     true
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? false
                                 :placeholder   "-"
                                 :value-path    [:website-content :editor/edited-item :about-us-section]}]))

(defn- about-us-section-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [common/surface-box ::about-us-section-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [about-us-section-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :about-us-section}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- about-us-page-picker
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [contents/content-picker ::about-us-page-picker
                                {:autosave?     true
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? false
                                 :placeholder   "-"
                                 :value-path    [:website-content :editor/edited-item :about-us-page]}]))

(defn- about-us-page-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [common/surface-box ::about-us-page-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [about-us-page-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :indent    {:top :m}
                            :label     :about-us-page}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- about-us
  []
  [:<> [about-us-section-box]
       [about-us-page-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- address-data-information-picker
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [contents/content-picker ::address-data-information-picker
                                {:autosave?     true
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? false
                                 :placeholder   "-"
                                 :value-path    [:website-content :editor/edited-item :address-data-information]}]))

(defn- address-data-information-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [common/surface-box ::address-data-information-box
                           {:indent  {:top :m}
                            :content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [address-data-information-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :address-data-information}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- contacts-data-information-picker
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [contents/content-picker ::contacts-data-information-picker
                                {:autosave?     true
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? false
                                 :placeholder   "-"
                                 :value-path    [:website-content :editor/edited-item :contacts-data-information]}]))

(defn- contacts-data-information-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [common/surface-box ::contacts-data-information-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [contacts-data-information-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :contacts-data-information}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- contacts
  []
  [:<> [contacts-data-information-box]
       [address-data-information-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- webshop-link-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [elements/text-field ::webshop-link-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :link
                             :placeholder :webshop-link-placeholder
                             :value-path  [:website-content :editor/edited-item :webshop-link]}]))

(defn- webshop-settings-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [common/surface-box ::webshop-settings-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 50})
                                                      [webshop-link-field]]
                                                [:div (forms/form-block-attributes {:ratio 50})]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :webshop}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- webshop
  []
  [:<> [webshop-settings-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- duplicate-brand-button
  [brand-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [elements/button {:color     :primary
                         :disabled? editor-disabled?
                         :font-size :xs
                         :indent    {:right :s :top :m}
                         :label     :duplicate!
                         :on-click  [:db/apply-item! [:website-content :editor/edited-item :brands]
                                                     vector/duplicate-nth-item brand-dex]}]))

(defn- delete-brand-button
  [brand-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [elements/button {:color     :warning
                         :disabled? editor-disabled?
                         :font-size :xs
                         :indent    {:right :s :top :m}
                         :label     :delete!
                         :on-click  [:db/apply-item! [:website-content :editor/edited-item :brands]
                                                     vector/remove-nth-item brand-dex]}]))

(defn- brand-description-field
  [brand-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [elements/multiline-field {:disabled?   editor-disabled?
                                  :label       :description
                                  :indent      {:top :m :vertical :s}
                                  :placeholder :brand-description-placeholder
                                  :value-path  [:website-content :editor/edited-item :brands brand-dex :description]}]))

(defn- brand-icon-picker
  [brand-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [storage/media-picker {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:top :m :vertical :s}
                              :label         :icon
                              :multi-select? false
                              :toggle-label  :select-image!
                              :thumbnail     {:height :3xl :width :5xl}
                              :value-path    [:website-content :editor/edited-item :brands brand-dex :icon]}]))

(defn- brand-title-field
  [brand-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [elements/text-field {:disabled?   editor-disabled?
                             :label       :section-title
                             :indent      {:top :m :vertical :s}
                             :placeholder :section-title-placeholder
                             :value-path  [:website-content :editor/edited-item :brands brand-dex :title]}]))

(defn- brand-link-label-field
  [brand-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [elements/text-field {:disabled?   editor-disabled?
                             :label       :link-label
                             :indent      {:top :m :vertical :s}
                             :placeholder :brand-name
                             :value-path  [:website-content :editor/edited-item :brands brand-dex :link-label]}]))

(defn- brand-link-field
  [brand-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [elements/text-field {:disabled?   editor-disabled?
                             :label       :link
                             :indent      {:top :m :vertical :s}
                             :placeholder :website-link-placeholder
                             :value-path  [:website-content :editor/edited-item :brands brand-dex :link]}]))

(defn- brand-box
  [brand-dex brand-props]
  [common/surface-box {:indent  {:top :m}
                       :content [:<> [:div (forms/form-row-attributes)
                                           [:div (forms/form-block-attributes {:ratio 30})
                                                 [brand-icon-picker      brand-dex brand-props]]
                                           [:div (forms/form-block-attributes {:ratio 70})
                                                 [brand-title-field      brand-dex brand-props]
                                                 [brand-link-label-field brand-dex brand-props]
                                                 [brand-link-field       brand-dex brand-props]]]
                                     [:div (forms/form-row-attributes)
                                           [:div (forms/form-block-attributes {:ratio 100})
                                                 [brand-description-field brand-dex brand-props]]]
                                     [:div {:style {:display :flex :justify-content :flex-end}}
                                           [duplicate-brand-button brand-dex brand-props]
                                           [delete-brand-button    brand-dex brand-props]]
                                     [elements/horizontal-separator {:size :xs}]]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- brand-list
  []
  (letfn [(f [%1 %2 %3] (conj %1 [brand-box %2 %3]))]
         (let [brands @(r/subscribe [:db/get-item [:website-content :editor/edited-item :brands]])]
              (reduce-kv f [:<>] brands))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- brand-controls-action-bar
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])
        on-click [:db/apply-item! [:website-content :editor/edited-item :brands] vector/cons-item {}]]
       [common/action-bar ::brand-controls-action-bar
                          {:disabled? editor-disabled?
                           :label     :add-brand!
                           :on-click  on-click}]))

(defn- selling-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [common/surface-box ::selling-box
                           {:content [:<> [brand-controls-action-bar]
                                          [elements/horizontal-separator {:size :xs}]]
                            :disabled? editor-disabled?
                            :label     :selling}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- selling
  []
  [:<> [selling-box]
       [brand-list]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- rent-informations-picker
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [contents/content-picker ::rent-informations-picker
                                {:autosave?     true
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? false
                                 :placeholder   "-"
                                 :value-path    [:website-content :editor/edited-item :rent-informations]}]))

(defn- rent-informations-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [common/surface-box ::rent-informations-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [rent-informations-picker]]
                                                [:div (forms/form-block-attributes {:ratio 67})]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :rent-informations}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- renting
  []
  [:<> [rent-informations-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [common/file-editor-menu-bar :website-content.editor
                                    {:menu-items [{:label :renting  :change-keys [:rent-informations]}
                                                  {:label :selling  :change-keys [:brands]}
                                                  {:label :webshop  :change-keys [:webshop-link]}
                                                  {:label :contacts :change-keys [:address-data-information :contacts-data-information]}
                                                  {:label :about-us :change-keys [:about-us-section :about-us-page]}]
                                     :disabled? editor-disabled?}]))

(defn- body
  []
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :website-content.editor])]
       (case current-view-id :renting  [renting]
                             :selling  [selling]
                             :webshop  [webshop]
                             :contacts [contacts]
                             :about-us [about-us])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [common/file-editor-controls :website-content.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [common/surface-breadcrumbs :website-content.editor/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :website-content}]
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-content.editor])]
       [common/surface-label :website-content.editor/view
                             {:disabled? editor-disabled?
                              :label     :website-content}]))

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
       [body]])

(defn- website-content-editor
  []
  [file-editor/body :website-content.editor
                    {:content-path  [:website-content :editor/edited-item]
                     :form-element  #'view-structure
                     :error-element [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element #'common/file-editor-ghost-element}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'website-content-editor}])
