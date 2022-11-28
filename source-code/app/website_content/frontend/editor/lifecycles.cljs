
(ns app.website-content.frontend.editor.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group-name :website
                                                            :icon       :wysiwyg
                                                            :icon-color "#8655b1"
                                                            :label      :website-content
                                                            :on-click   [:x.router/go-to! "/@app-home/website-content"]
                                                            :horizontal-weight 3}]
                              [:home.sidebar/add-menu-item! {:group-name :website
                                                             :icon       :wysiwyg
                                                             :icon-color "#8655b1"
                                                             :label      :website-content
                                                             :on-click   [:x.router/go-to! "/@app-home/website-content"]
                                                             :vertical-weight 3}]]}})
