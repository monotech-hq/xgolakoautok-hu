
(ns app.website-config.frontend.editor.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.file-editor.api  :as file-editor]
              [forms.api                :as forms]
              [layouts.surface-a.api    :as surface-a]
              [mid-fruits.css           :as css]
              [mid-fruits.vector        :as vector]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- company-slogan-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/text-field ::company-slogan-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :slogan
                             :placeholder :company-slogan-placeholder
                             :value-path  [:website-config :editor/edited-item :company-slogan]}]))

(defn- company-name-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/text-field ::company-name-field
                            {:autofocus?  true
                             :disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :name
                             :placeholder :company-name-placeholder
                             :value-path  [:website-config :editor/edited-item :company-name]}]))

(defn- company-data-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [common/surface-box ::company-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [company-name-field]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [company-slogan-field]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :indent    {:top :m}
                            :label     :company-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- company-logo-picker
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [storage/media-picker ::company-logo-picker
                             {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? false
                              :placeholder   "-"
                              :toggle-label  :select-image!
                              :thumbnail     {:height :3xl :width :5xl}
                              :value-path    [:website-config :editor/edited-item :company-logo]}]))

(defn- company-logo-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [common/surface-box ::company-logo-box
                           {:content [:<> [company-logo-picker]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :company-logo}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- basic-data
  []
  [:<> [company-logo-box]
       [company-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- duplicate-contact-group-button
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/button {:color     :primary
                         :disabled? editor-disabled?
                         :font-size :xs
                         :indent    {:right :s :top :m}
                         :label     :duplicate!
                         :on-click  [:db/apply-item! [:website-config :editor/edited-item :contact-groups]
                                                     vector/duplicate-nth-item group-dex]}]))

(defn- delete-contact-group-button
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/button {:color     :warning
                         :disabled? editor-disabled?
                         :font-size :xs
                         :indent    {:right :s :top :m}
                         :label     :delete!
                         :on-click  [:db/apply-item! [:website-config :editor/edited-item :contact-groups]
                                                     vector/remove-nth-item group-dex]}]))

(defn- contact-group-label-field
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/text-field {:disabled?   editor-disabled?
                             :label       :label
                             :indent      {:top :m :vertical :s}
                             :placeholder :contacts-label-placeholder
                             :value-path  [:website-config :editor/edited-item :contact-groups group-dex :label]}]))

(defn- email-addresses-field
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/multi-field {:disabled?   editor-disabled?
                              :label       :email-address
                              :indent      {:top :m :vertical :s}
                              :placeholder :email-address-placeholder
                              :value-path  [:website-config :editor/edited-item :contact-groups group-dex :email-addresses]}]))

(defn- phone-numbers-field
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/multi-field {:disabled?   editor-disabled?
                              :label       :phone-number
                              :indent      {:top :m :vertical :s}
                              :placeholder :phone-number-placeholder
                              :value-path  [:website-config :editor/edited-item :contact-groups group-dex :phone-numbers]}]))

(defn- contact-group-box
  [group-dex group-props]
  [common/surface-box {:content [:<> [:div (forms/form-row-attributes)
                                           [:div (forms/form-block-attributes {:ratio 100})
                                                 [contact-group-label-field group-dex group-props]]
                                      [:div (forms/form-row-attributes)
                                           [:div (forms/form-block-attributes {:ratio 50})
                                                 [phone-numbers-field group-dex group-props]]
                                           [:div (forms/form-block-attributes {:ratio 50})
                                                 [email-addresses-field group-dex group-props]]]]
                                     [:div {:style {:display :flex :justify-content :flex-end}}
                                           [duplicate-contact-group-button group-dex group-props]
                                           [delete-contact-group-button    group-dex group-props]]
                                     [elements/horizontal-separator {:size :xs}]]
                       :indent {:top :m}}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- contact-group-list
  []
  (letfn [(f [%1 %2 %3] (conj %1 [contact-group-box %2 %3]))]
         (let [contact-groups @(r/subscribe [:db/get-item [:website-config :editor/edited-item :contact-groups]])]
              (reduce-kv f [:<>] contact-groups))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- contacts-controls-action-bar
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])
        on-click [:db/apply-item! [:website-config :editor/edited-item :contact-groups] vector/cons-item {}]]
       [common/action-bar ::contacts-controls-action-bar
                          {:disabled? editor-disabled?
                           :label     :add-contacts-data!
                           :on-click  on-click}]))

(defn- contacts-data-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [common/surface-box ::contacts-data-box
                           {:content [:<> [contacts-controls-action-bar]
                                          [elements/horizontal-separator {:size :xs}]]
                            :disabled? editor-disabled?
                            :label     :contacts-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- contacts-data
  []
  [:<> [contacts-data-box]
       [contact-group-list]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- duplicate-address-group-button
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/button {:color     :primary
                         :disabled? editor-disabled?
                         :font-size :xs
                         :indent    {:right :s :top :m}
                         :label     :duplicate!
                         :on-click  [:db/apply-item! [:website-config :editor/edited-item :address-groups]
                                                     vector/duplicate-nth-item group-dex]}]))

(defn- delete-address-group-button
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/button {:color     :warning
                         :disabled? editor-disabled?
                         :font-size :xs
                         :indent    {:right :s :top :m}
                         :label     :delete!
                         :on-click  [:db/apply-item! [:website-config :editor/edited-item :address-groups]
                                                     vector/remove-nth-item group-dex]}]))

(defn- address-group-label-field
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/text-field {:disabled?   editor-disabled?
                             :label       :label
                             :indent      {:top :m :vertical :s}
                             :placeholder :address-label-placeholder
                             :value-path  [:website-config :editor/edited-item :address-groups group-dex :label]}]))

(defn- company-address-field
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/text-field {:disabled?   editor-disabled?
                             :label       :address
                             :indent      {:top :m :vertical :s}
                             :placeholder :full-address-placeholder
                             :value-path  [:website-config :editor/edited-item :address-groups group-dex :company-address]}]))

(defn- google-maps-link
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])
        company-address  @(r/subscribe [:db/get-item [:website-config :editor/edited-item :address-groups group-dex :company-address]])]
       [common/data-element {:disabled? editor-disabled?
                             :label     :google-maps-link
                             :value     (str "https://www.google.com/maps/search/?api=1&query=" company-address)
                             :indent    {:top :xxl :vertical :s}}]))

(defn- google-maps-link-toggle
  [group-dex _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])
        company-address  @(r/subscribe [:db/get-item [:website-config :editor/edited-item :address-groups group-dex :company-address]])
        company-address-link (str "https://www.google.com/maps/search/?api=1&query=" company-address)]
       [:div {:style {:display :flex}}
             [elements/button {:color     :primary
                               :disabled? editor-disabled?
                               :font-size :xs
                               :label     :open-link!
                               :on-click  {:fx [:environment/open-new-browser-tab! company-address-link]}
                               :indent    {:vertical :s}}]]))

(defn- address-group-box
  [group-dex group-props]
  [common/surface-box {:indent  {:top :m}
                       :content [:<> [:div (forms/form-row-attributes)
                                           [:div (forms/form-block-attributes {:ratio 100})
                                                 [address-group-label-field group-dex group-props]]
                                      [:div (forms/form-row-attributes)
                                           [:div (forms/form-block-attributes {:ratio 100})
                                                 [company-address-field group-dex group-props]]]]
                                     [:div (forms/form-row-attributes)
                                           [:div (forms/form-block-attributes {:ratio 100})
                                                 [google-maps-link        group-dex group-props]
                                                 [google-maps-link-toggle group-dex group-props]]]
                                     [:div {:style {:display :flex :justify-content :flex-end}}
                                           [duplicate-address-group-button group-dex group-props]
                                           [delete-address-group-button    group-dex group-props]]
                                     [elements/horizontal-separator {:size :xs}]]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- address-group-list
  []
  (letfn [(f [%1 %2 %3] (conj %1 [address-group-box %2 %3]))]
         (let [address-groups @(r/subscribe [:db/get-item [:website-config :editor/edited-item :address-groups]])]
              (reduce-kv f [:<>] address-groups))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- address-controls-action-bar
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])
        on-click [:db/apply-item! [:website-config :editor/edited-item :address-groups] vector/cons-item {}]]
       [common/action-bar ::address-controls-action-bar
                          {:disabled? editor-disabled?
                           :label     :add-address-data!
                           :on-click  on-click}]))

(defn- address-data-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [common/surface-box ::address-data-box
                           {:content [:<> [address-controls-action-bar]
                                          [elements/horizontal-separator {:size :xs}]]
                            :disabled? editor-disabled?
                            :label     :address-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- address-data
  []
  [:<> [address-data-box]
       [address-group-list]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- facebook-links-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/multi-field ::facebook-links-field
                             {:autofocus?  true
                              :disabled?   editor-disabled?
                              :indent      {:top :m :vertical :s}
                              :label       :facebook-link
                              :placeholder :facebook-link-placeholder
                              :value-path  [:website-config :editor/edited-item :facebook-links]}]))

(defn- instagram-links-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/multi-field ::instagram-links-field
                             {:disabled?   editor-disabled?
                              :indent      {:top :m :vertical :s}
                              :label       :instagram-link
                              :placeholder :instagram-link-placeholder
                              :value-path  [:website-config :editor/edited-item :instagram-links]}]))

(defn- youtube-links-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/multi-field ::youtube-links-field
                             {:disabled?   editor-disabled?
                              :indent      {:top :m :vertical :s}
                              :label       :youtube-link
                              :placeholder :youtube-link-placeholder
                              :value-path  [:website-config :editor/edited-item :youtube-links]}]))

(defn- social-media-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [common/surface-box ::social-media-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [facebook-links-field]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [instagram-links-field]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [youtube-links-field]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :social-media}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- social-media
  []
  [:<> [social-media-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- meta-name-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/text-field ::meta-name-field
                            {:autofocus?  true
                             :disabled?   editor-disabled?
                             :label       :meta-name
                             :indent      {:top :m :vertical :s}
                             :info-text   :describe-the-page-with-a-name
                             :placeholder :meta-name-placeholder
                             :value-path  [:website-config :editor/edited-item :meta-name]}]))

(defn- meta-title-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/text-field ::meta-title-field
                            {:disabled?   editor-disabled?
                             :label       :meta-title
                             :indent      {:top :m :vertical :s}
                             :info-text   :describe-the-page-with-a-short-title
                             :placeholder :meta-title-placeholder
                             :value-path  [:website-config :editor/edited-item :meta-title]}]))

(defn- meta-description-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/multiline-field ::meta-description-field
                                 {:disabled?   editor-disabled?
                                  :label       :meta-description
                                  :indent      {:top :m :vertical :s}
                                  :info-text   :describe-the-page-with-a-short-description
                                  :placeholder :meta-description-placeholder
                                  :value-path  [:website-config :editor/edited-item :meta-description]}]))

(defn- meta-keywords-field
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [elements/multi-combo-box ::meta-keywords-field
                                 {:deletable?  true
                                  :disabled?   editor-disabled?
                                  :label       :meta-keywords
                                  :indent      {:top :m :vertical :s}
                                  :info-text   :describe-the-page-in-a-few-keywords
                                  :placeholder :meta-keywords-placeholder
                                  :value-path  [:website-config :editor/edited-item :meta-keywords]}]))

(defn- seo-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [common/surface-box ::seo-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [meta-name-field]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [meta-title-field]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [meta-keywords-field]]
                                           [:div (forms/form-row-attributes)
                                                 [:div (forms/form-block-attributes {:ratio 100})
                                                       [meta-description-field]]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :seo}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- seo
  []
  [:<> [seo-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- share-preview-picker
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [storage/media-picker ::share-preview-picker
                             {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? false
                              :placeholder   "-"
                              :toggle-label  :select-image!
                              :thumbnail     {:height :3xl :width :5xl}
                              :value-path    [:website-config :editor/edited-item :share-preview]}]))

(defn- share-preview-box
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [common/surface-box ::share-preview-box
                           {:content [:<> [share-preview-picker]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :info-text {:content :recommended-image-size-n :replacements ["1200" "630"]}
                            :label     :share-preview}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- share
  []
  [:<> [share-preview-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :website-config.editor])]
       (case current-view-id :basic-data    [basic-data]
                             :contacts-data [contacts-data]
                             :address-data  [address-data]
                             :social-media  [social-media]
                             :seo           [seo]
                             :share         [share])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [common/file-editor-menu-bar :website-config.editor
                                    {:menu-items [{:label :basic-data    :change-keys [:company-name :company-slogan :company-logo]}
                                                  {:label :contacts-data :change-keys [:contact-groups]}
                                                  {:label :address-data  :change-keys [:address-groups]}
                                                  {:label :social-media  :change-keys [:facebook-links :instagram-links :youtube-links]}
                                                  {:label :seo           :change-keys [:meta-name :meta-title :meta-keywords :meta-description]}
                                                  {:label :share         :change-keys [:share-preview]}]
                                     :disabled? editor-disabled?}]))

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [common/file-editor-controls :website-config.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [common/surface-breadcrumbs :website-config.editor/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :website-config}]
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? :website-config.editor])]
       [common/surface-label :website-config.editor/view
                             {:disabled? editor-disabled?
                              :label     :website-config}]))

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

(defn- website-config-editor
  []
  [file-editor/body :website-config.editor
                    {:content-path  [:website-config :editor/edited-item]
                     :form-element  #'view-structure
                     :error-element [common/error-content {:error :the-content-you-opened-may-be-broken}]
                     :ghost-element #'common/file-editor-ghost-element}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'website-config-editor}])
