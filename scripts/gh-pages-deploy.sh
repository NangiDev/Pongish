#!/bin/bash
dist_dir="html/build/dist/"
dist_branch="gh-pages"
git checkout --orphan $dist_branch
echo "Building dist..."
./gradlew html:dist
git --work-tree $dist_dir add --all
git --work-tree $dist_dir commit -m $dist_branch
echo "Pushing to gh-pages..."
git push origin HEAD:$dist_branch --force
rm -r $dist_dir
git checkout -f main
git branch -D $dist_branch
echo "Successfully deployed..."