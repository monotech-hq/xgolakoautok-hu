
(ns app.website-post.frontend.editor.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group-name :website
                                                            :icon       :mail
                                                            :icon-color "#8655b1"
                                                            :icon-family :material-icons-outlined
                                                            :label      :website-post
                                                            :on-click   [:x.router/go-to! "/@app-home/website-post"]
                                                            :horizontal-weight 3}]
                              [:home.sidebar/add-menu-item! {:group-name :website
                                                             :icon       :mail
                                                             :icon-color "#8655b1"
                                                             :icon-family :material-icons-outlined
                                                             :label      :website-post
                                                             :on-click   [:x.router/go-to! "/@app-home/website-post"]
                                                             :vertical-weight 3}]]}})
